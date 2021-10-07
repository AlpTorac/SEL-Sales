package external.connection.outgoing;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.timeout.ITimeoutStrategy;
import external.message.IMessage;

public abstract class SendBuffer implements ISendBuffer {
	
	private volatile boolean isBlocked = false;
	
	private IMessageSendingStrategy mss;
	private ISendBufferDataContainer buffer;
	private IConnection conn;
	
	private ITimeoutStrategy ts;
	
	private int currentSequenceNumber;
	
	SendBuffer(IConnection conn, IMessageSendingStrategy mss, ISendBufferDataContainer buffer, ITimeoutStrategy ts, ExecutorService es) {
		this.currentSequenceNumber = 0;
		this.conn = conn;
		this.mss = mss;
		this.buffer = buffer;
		this.ts = ts;
		this.ts.setNotifyTarget(this);
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
		this.block();
		return this.sendMessageAndStartTimeoutTimer();
	}
	/**
	 * Re-sends the last message that could not be sent, called via timeout.
	 * @return Whether the message is successfully sent.
	 */
	protected boolean resendLast() {
		boolean result = this.mss.sendMessage(this.conn, this.buffer.getMessageInLine());
		this.startTimeoutTimer();
		return result;
	}

	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}

	@Override
	public void timeout() {
		this.resendLast();
	}
	
	@Override
	public void timeoutTimerStopped() {}
	
	protected void startTimeoutTimer() {
		this.ts.startTimer();
		System.out.println("startTimeoutTimer() in " + this);
	}
	
	private void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	private boolean sendMessageAndStartTimeoutTimer() {
		IMessage messageInLine = this.buffer.getMessageInLine();
		messageInLine.setSequenceNumber(this.currentSequenceNumber);
		boolean isSent = this.mss.sendMessage(this.conn, messageInLine);
		if (isSent) {
			this.startTimeoutTimer();
		}
		return isSent;
	}
	
	@Override
	public void receiveAcknowledgement(IMessage message) {
		if (!this.isEmpty() && message.isAcknowledgementMessage() 
				&& this.buffer.getMessageInLine().getSequenceNumber() == message.getSequenceNumber()) {
			this.ts.reset();
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
		System.out.println("unblock() in " + this);
		this.setBlocked(false);
	}
	@Override
	public void close() {
		
	}
	@Override
	public boolean isEmpty() {
		return this.buffer.isEmpty();
	}
}
