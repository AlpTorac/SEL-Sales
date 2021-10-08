package external.connection.pingpong;

import java.util.concurrent.ExecutorService;

import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.timeout.ITimeoutStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;

public abstract class PingPong implements IPingPong {
	
	private volatile boolean isBlocked = false;
	
	private IMessageSendingStrategy mss;
	private ITimeoutStrategy ts;
	private volatile int resendCount;
	private int resendLimit;
	private IConnection conn;
	
	private DisconnectionListener disconListener;
	
	protected PingPong(IConnection conn, IMessageSendingStrategy mss, ITimeoutStrategy ts, ExecutorService es, long minimalDelay, int resendLimit) {
		this.conn = conn;
		this.mss = mss;
		this.ts = ts;
		this.resendCount = 0;
		this.resendLimit = resendLimit;
		this.ts.setNotifyTarget(this);
	}
	
	@Override
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.disconListener = dl;
	}
	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}
	protected void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	protected void unblock() {
		this.setBlocked(false);
	}
	protected void block() {
		this.setBlocked(true);
	}
	protected IMessage generatePingPongMessage() {
		return new Message(MessageContext.PINGPONG, null, null);
	}
	
	public boolean sendPingPongMessage() {
		if (this.isBlocked()) {
			return false;
		}
		this.block();
//		if (this.getRemainingResendTries() <= 0) {
//			this.reportDisconnection();
//			return false;
//		}
		return this.sendPingPongMessageAndStartTimer();
	}
	
	protected boolean sendPingPongMessageAndStartTimer() {
//		if (this.getRemainingResendTries() <= 0) {
//			this.reportDisconnection();
//			return false;
//		}
		boolean isSent = this.mss.sendMessage(conn, this.generatePingPongMessage());
		if (isSent) {
			this.startTimeoutTimer();
//			System.out.println("Ping pong message sent");
		}
		return isSent;
	}
	@Override
	public void startTimeoutTimer() {
//		System.out.println("Ping pong start timer");
		this.ts.startTimer();
	}
	
	protected void resetTimeoutTimer() {
//		System.out.println("Ping pong reset timer");
		this.ts.reset();
	}
	
	protected void resetResendCount() {
//		System.out.println("Ping pong reset resend count");
		this.resendCount = 0;
	}
	
	protected void resendPingPongMessage() {
//		System.out.println("Ping pong resend, count = " + this.resendCount);
		this.increaseResendCount();
		this.sendPingPongMessageAndStartTimer();
	}
	
	protected void increaseResendCount() {
//		System.out.println("Ping pong resend count ++");
		this.resendCount = this.resendCount + 1;
	}
	
	protected void reportDisconnection() {
//		System.out.println("Reporting disconnection");
		if (this.disconListener != null) {
//			System.out.println("Disconnection listener not null");
			this.disconListener.connectionLost(conn.getTargetClientAddress());
//			System.out.println("Reported disconnection");
		}
	}
	
	@Override
	public void receiveResponse(IMessage message) {
//		System.out.println("Ping pong receive response");
		this.resetTimeoutTimer();
		this.resetResendCount();
	}
	
	@Override
	public void timeout() {
//		System.out.println("Ping pong timeout");
		if (this.getRemainingResendTries() > 0) {
			this.resendPingPongMessage();
		} else {
			this.reportDisconnection();
		}
	}
	
	@Override
	public void timeoutTimerStopped(boolean wasReset) {
		if (wasReset) {
			this.unblock();
		}
	}
	
	@Override
	public void close() {
//		System.out.println("Ping pong close");
		this.ts.reset();
	}
	
//	@Override
//	public boolean isRunning() {
//		System.out.println("Ping pong isRunning");
//		return this.ts.isRunning();
//	}
	
	@Override
	public int getRemainingResendTries() {
//		System.out.println("Ping pong getRemainingTries");
		return this.resendLimit - this.resendCount;
	}
}
