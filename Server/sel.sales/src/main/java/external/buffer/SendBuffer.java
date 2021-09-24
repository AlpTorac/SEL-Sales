package external.buffer;

import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import external.connection.IMessageSendingStrategy;
import external.message.IMessage;

public abstract class SendBuffer implements ISendBuffer {
	
	private volatile boolean isBlocked = false;
	
	private IMessageSendingStrategy mss;
	private ISendBufferDataContainer buffer;
	private OutputStream os;
	
	private ExecutorService es;
	private ITimeoutStrategy ts;
	
	private int currentSequenceNumber;
	
	SendBuffer(OutputStream os, IMessageSendingStrategy mss, ISendBufferDataContainer buffer, ITimeoutStrategy ts, ExecutorService es) {
		this.currentSequenceNumber = 0;
		this.os = os;
		this.mss = mss;
		this.buffer = buffer;
		this.ts = ts;
		this.es = es;
		this.initTimeoutTimer();
	}
	
	@Override
	public void addMessage(IMessage message) {
		this.buffer.add(message);
	}
	
	@Override
	public boolean sendMessage() {
		if (this.isBlocked() || this.buffer.isEmpty()) {
			return false;
		}
		return this.sendMessageAndStartTimeoutTimer();
	}
	/**
	 * Re-sends the last message that could not be sent, called via timeout.
	 * @return Whether the message is successfully sent.
	 */
	protected boolean resendLast() {
		return this.mss.sendMessage(this.os, this.buffer.getMessageInLine());
	}

	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}

	@Override
	public void timeout() {
		this.resendLast();
	}
	
	protected void initTimeoutTimer() {
		this.ts.setNotifyTarget(this);
		this.es.submit(this.ts);
	}
	
	protected void startTimeoutTimer() {
		this.ts.startTimer();
	}
	
	private void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	private boolean sendMessageAndStartTimeoutTimer() {
		this.block();
		IMessage messageInLine = this.buffer.getMessageInLine();
		messageInLine.setSequenceNumber(this.currentSequenceNumber);
		boolean isSent = this.mss.sendMessage(this.os, messageInLine);
		if (isSent) {
			this.ts.startTimer();
		}
		return isSent;
	}
	
	@Override
	public void receiveAcknowledgement(IMessage message) {
		if (message.isAcknowledgementMessage() 
				&& this.buffer.getMessageInLine().getSequenceNumber() == message.getSequenceNumber()) {
			this.buffer.removeMessageInLine();
			this.currentSequenceNumber = this.currentSequenceNumber + 1;
			this.unblock();
		}
	}
	/**
	 * Stop the buffer from sending any more messages except repeating the last sent message in case of timeout,
	 * until an appropriate acknowledgement is received.
	 */
	protected void block() {
		this.setBlocked(true);
	}
	/**
	 * Allow the buffer to send messages.
	 */
	protected void unblock() {
		this.ts.reset();
		this.setBlocked(false);
	}
	@Override
	public void close() {
		this.ts.terminate();
	}
	@Override
	public boolean isEmpty() {
		return this.buffer.isEmpty();
	}
}
