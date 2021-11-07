package external.connection.outgoing;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.timeout.ITimeoutStrategy;
import external.message.IMessage;

public abstract class SendBuffer implements ISendBuffer {
	private volatile boolean isClosed = false;
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
	public ITimeoutStrategy getTimeoutStrategy() {
		return this.ts;
	}
	
	@Override
	public boolean isClosed() {
		return this.isClosed;
	}
	
	@Override
	public void addMessage(IMessage message) {
		if (!this.isClosed()) {
			IMessage m = message.clone();
			m.setSequenceNumber(this.getCurrentSequenceNumber());
			this.buffer.add(m);
		}
	}
	
	@Override
	public boolean hasRunningTimer() {
		return this.ts.hasRunningTimer();
	}
	
	@Override
	public boolean sendMessage() {
		if (!this.hasRunningTimer() && this.messageInLine != null && this.isBlocked()) {
			return this.resendLast();
		}
		if (this.isClosed() || this.isBlocked() || this.hasRunningTimer() || this.messageInLine != null  || this.buffer.isEmpty()) {
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
	public void timeoutTimerStopped(boolean wasReset, boolean wasTerminated) {
		if (!wasReset && !wasTerminated) {
//			System.out.println("Timeout, resending last in " + this);
//			this.resendLast();
		} else if (wasReset && !wasTerminated) {
			
		}
	}
	
	protected void startTimeoutTimer() {
//		while (this.ts.hasRunningTimer()) {
//			
//		}
		this.ts.startTimer();
//		System.out.println("startTimeoutTimer() in " + this);
	}
	
	private void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	private boolean sendMessageAndStartTimeoutTimer() {
		if (this.isClosed()) {
			return false;
		}
		boolean isSent = this.mss.sendMessage(this.conn, this.messageInLine);
//		if (isSent) {
//			this.startTimeoutTimer();
//		}
		System.out.println("Sent sequence number: " + this.getCurrentSequenceNumber());
		this.startTimeoutTimer();
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
//		System.out.println("Acknowledgement received, " + "seqNumbers = " + this.currentSequenceNumber + ", " + message.getSequenceNumber());
		if (this.messageInLine != null && message.isAcknowledgementMessage() && this.getCurrentSequenceNumber() == message.getSequenceNumber()) {
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
		System.out.println("send buffer closed");
		this.isClosed = true;
		this.messageInLine = null;
		this.buffer.clear();
		this.ts.terminateTimer();
		this.ts = null;
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
