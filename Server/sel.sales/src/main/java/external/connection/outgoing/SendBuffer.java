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
	
	private volatile int currentSequenceNumber = 0;
	
	private IMessage messageInLine = null;
	
	SendBuffer(IConnection conn, IMessageSendingStrategy mss, ISendBufferDataContainer buffer, ITimeoutStrategy ts, ExecutorService es) {
		this.conn = conn;
		this.mss = mss;
		this.buffer = buffer;
		this.ts = ts;
		this.ts.setNotifyTarget(this);
	}
	
	@Override
	public void addMessage(IMessage message) {
		IMessage m = message.clone();
		m.setSequenceNumber(this.getCurrentSequenceNumber());
		this.buffer.add(m);
	}
	
	@Override
	public boolean sendMessage() {
		if (this.messageInLine != null || this.isBlocked() || this.buffer.isEmpty()) {
			return false;
		}
		this.block();
		this.messageInLine = this.buffer.getMessageInLine();
		this.messageInLine.setSequenceNumber(this.getCurrentSequenceNumber());
		return this.sendMessageAndStartTimeoutTimer();
	}
	/**
	 * Re-sends the last message that could not be sent, called via timeout.
	 * @return Whether the message is successfully sent.
	 */
	protected boolean resendLast() {
		return this.sendMessageAndStartTimeoutTimer();
	}

	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}

	@Override
	public void timeout() {

	}
	
	@Override
	public void timeoutTimerStopped(boolean wasReset) {
		if (!wasReset) {
//			System.out.println("Timeout, resending last in " + this);
			this.resendLast();
		}
	}
	
	protected void startTimeoutTimer() {
		while (this.ts.hasRunningTimer()) {
			
		}
		this.ts.startTimer();
//		System.out.println("startTimeoutTimer() in " + this);
	}
	
	private void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	private boolean sendMessageAndStartTimeoutTimer() {
		boolean isSent = this.mss.sendMessage(this.conn, this.messageInLine);
		if (isSent) {
			this.startTimeoutTimer();
		}
		return isSent;
	}
	
	protected void prepareNext() {
		this.buffer.remove(this.messageInLine);
		this.messageInLine = null;
		this.currentSequenceNumber = this.currentSequenceNumber + 1;
		this.unblock();
	}
	
	@Override
	public void receiveAcknowledgement(IMessage message) {
//		System.out.println("Acknowledgement received, verifying in " + this);
		if (this.messageInLine != null && message.isAcknowledgementMessage() && this.messageInLine.getSequenceNumber() == message.getSequenceNumber()) {
//			System.out.println("Acknowledgement received, unblocking " + this);
			this.ts.reset();
			this.prepareNext();
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
//		System.out.println("unblock() in " + this);
		this.setBlocked(false);
	}
	@Override
	public void close() {
		
	}
	@Override
	public boolean isEmpty() {
		return this.buffer.isEmpty();
	}
	
	@Override
	public int getCurrentSequenceNumber() {
		return this.currentSequenceNumber;
	}
}
