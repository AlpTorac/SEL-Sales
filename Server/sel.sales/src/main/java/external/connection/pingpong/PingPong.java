package external.connection.pingpong;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.timeout.ITimeoutStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;

public abstract class PingPong implements IPingPong {
	private IMessageSendingStrategy mss;
	private ITimeoutStrategy ts;
	private ExecutorService es;
	private int resendCount;
	private int resendLimit;
	private IConnection conn;
	
	private boolean isBlocked = false;
	
	private DisconnectionListener disconListener;
	
	protected PingPong(IConnection conn, IMessageSendingStrategy mss, ITimeoutStrategy ts, ExecutorService es, int resendLimit) {
		this.conn = conn;
		this.mss = mss;
		this.ts = ts;
		this.es = es;
		this.resendCount = 0;
		this.resendLimit = resendLimit;
		this.initTimeoutTimer();
	}
	
	protected void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	protected void block() {
		this.setBlocked(true);
	}
	
	protected void unblock() {
		this.resetTimeoutTimer();
		this.resetResendCount();
		this.setBlocked(false);
	}
	
	@Override
	public boolean isBlocked() {
		return this.isBlocked;
	}
	
	@Override
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.disconListener = dl;
	}
	
	protected IMessage generatePingPongMessage() {
		return new Message(MessageContext.PINGPONG, null, null);
	}
	
	public boolean sendPingPongMessage() {
		if (this.isBlocked()) {
			return false;
		}
		return this.sendPingPongMessageAndStartTimer();
	}
	
	protected boolean sendPingPongMessageAndStartTimer() {
		System.out.println("Sending ping message");
		boolean isSent = this.mss.sendMessage(conn, this.generatePingPongMessage());
		if (isSent) {
			this.block();
			this.startTimeoutTimer();
			System.out.println("Sent ping message");
		}
		return isSent;
	}
	
	protected void initTimeoutTimer() {
		this.ts.setNotifyTarget(this);
		this.es.submit(this.ts);
	}
	
	protected void startTimeoutTimer() {
		this.ts.startTimer();
	}
	
	protected void resetTimeoutTimer() {
		this.ts.reset();
	}
	
	protected void resetResendCount() {
		this.resendCount = 0;
	}
	
	protected void resendPingPongMessage() {
		this.increaseResendCount();
		this.sendPingPongMessage();
		System.out.println("Sending ping message, resend: " + resendCount);
	}
	
	protected void increaseResendCount() {
		this.resendCount = this.resendCount + 1;
	}
	
	@Override
	public void receiveResponse(IMessage message) {
		System.out.println("Received pong");
		this.unblock();
	}
	
	@Override
	public void timeout() {
		if (this.resendCount < this.resendLimit) {
			this.resendPingPongMessage();
		} else {
			this.disconListener.connectionLost(conn.getTargetClientAddress());
		}
	}
	
	@Override
	public void close() {
		this.ts.terminate();
		try {
			this.conn.close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isRunning() {
		return this.ts.isRunning();
	}
	
	@Override
	public int getRemainingResendTries() {
		return this.resendLimit - this.resendCount;
	}
}
