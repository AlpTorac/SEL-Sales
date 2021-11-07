package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import model.settings.ISettings;

public abstract class ConnectionManager implements IConnectionManager {
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeoutInMillis;
	private volatile long pingPongTimeoutInMillis;
	private volatile int resendLimit;
	
	private ExecutorService es;
	private IConnection conn;
	private IMessageReceptionist mr;
	private ISendBuffer sb;
	private IPingPong pingPong;
	protected IController controller;
	
	private volatile boolean isClosed = false;
	
	private DisconnectionListener disconListener;
	
//	private Thread cycleThread;
	
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es, long pingPongTimeoutInMillis, long sendTimeoutInMillis, int resendLimit, long minimalPingPongDelay) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
		this.pingPongTimeoutInMillis = pingPongTimeoutInMillis;
		this.sendTimeoutInMillis = sendTimeoutInMillis;
		this.resendLimit = resendLimit;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.init();
	}
	
	@Override
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.disconListener = dl;
		this.pingPong.setDisconnectionListener(this.disconListener);
	}
	
	@Override
	public IPingPong getPingPong() {
		return this.pingPong;
	}
	
	@Override
	public IConnection getConnection() {
		return this.conn;
	}
	
	protected ExecutorService getExecutorService() {
		return this.es;
	}
	
	protected void initPingPong(long minimalDelay, int resendLimit, long pingPongTimeoutInMillis) {
		this.pingPong = this.createPingPong(minimalDelay, resendLimit, pingPongTimeoutInMillis);
		this.pingPong.setDisconnectionListener(disconListener);
	}

	protected abstract IPingPong createPingPong(long minimalDelay, int resendLimit, long pingPongTimeoutInMillis);
	
	protected void initSendBuffer(long timeoutInMillis) {
		this.sb = this.createSendBuffer(timeoutInMillis);
	}
	
	protected abstract ISendBuffer createSendBuffer(long timeoutInMillis);
	
	protected void initMessageReceptionist(ISendBuffer sb, IPingPong pingPong) {
		IMessageReceptionist mr = this.createMessageReceptionist(sb, pingPong);
		this.mr = mr;
	}
	
	protected abstract IMessageReceptionist createMessageReceptionist(ISendBuffer sb, IPingPong pingPong);
	
	@Override
	public ISendBuffer getSendBuffer() {
		return this.sb;
	}
	@Override
	public IMessageReceptionist getIncomingMessageListener() {
		return this.mr;
	}
	
	protected void checkForMessagesToBeRead() {
		this.getIncomingMessageListener().checkForMessages();
//		System.out.println("Read attempt");
	}
	
	protected void checkForMessagesToBeSent() {
		this.getSendBuffer().sendMessage();
//		System.out.println("Send attempt");
	}
	
	protected void sendPingPongMessage() {
		this.getPingPong().sendPingPongMessage();
//		System.out.println("PingPong attempt");
	}
	@Override
	public boolean isClosed() {
		return this.isClosed;
	}
	
	protected void init() {
		System.out.println("Connection manager init");
//		this.initPingPong(this.getMinimalPingPongDelay(), this.getResendLimit(), this.getPingPongTimeout());
//		this.initSendBuffer(this.getSendTimeout());
		this.initPingPong(this.getMinimalPingPongDelay(), this.getPingPongResendLimit(), this.getPingPongTimeoutInMillis());
		this.initSendBuffer(this.getSendTimeoutInMillis());
		this.initMessageReceptionist(this.getSendBuffer(), this.getPingPong());
		System.out.println("Connection manager init end");
		System.out.println("Connection manager submitting runnable");
		this.es.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("Connection manager runnable being executed");
				while (!isClosed() && !getConnection().isClosed()) {
//					System.out.println("Connection manager cycle entered");
					checkForMessagesToBeRead();
					checkForMessagesToBeSent();
					sendPingPongMessage();
				}
			}
		});
		System.out.println("Connection manager submitted runnable");
	}
	
//	protected int getResendLimit() {
//		return this.resendLimit;
//	}
//	
//	protected long getSendTimeout() {
//		return this.sendTimeoutInMillis;
//	}
//	
//	protected long getPingPongTimeout() {
//		return this.pingPongTimeoutInMillis;
//	}
	
	@Override
	public void sendMessage(IMessage message) {
		this.getSendBuffer().addMessage(message);
	}
	
	@Override
	public void close() {
		System.out.println("Connection Manager closing");
		isClosed = true;
		try {
			this.getSendBuffer().close();
			this.getPingPong().close();
			this.getIncomingMessageListener().close();
			this.getConnection().close();
//			try {
//				cycleThread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public long getSendTimeoutInMillis() {
		return this.sendTimeoutInMillis;
	}

	@Override
	public long getPingPongTimeoutInMillis() {
		return this.pingPongTimeoutInMillis;
	}

	@Override
	public int getPingPongResendLimit() {
		return this.resendLimit;
	}
	
	@Override
	public long getMinimalPingPongDelay() {
		return this.minimalPingPongDelay;
	}

	@Override
	public void receiveSettings(ISettings settings) {
		
	}
	
	@Override
	public void setMinimalPingPongDelay(long minimalPingPongDelay) {
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.pingPong.setMinimalDelay(minimalPingPongDelay);
	}

	@Override
	public void setSendTimeoutInMillis(long sendTimeoutInMillis) {
		this.sendTimeoutInMillis = sendTimeoutInMillis;
		this.sb.setTimeUnitAmount(sendTimeoutInMillis);
	}

	@Override
	public void setPingPongTimeoutInMillis(long pingPongTimeoutInMillis) {
		this.pingPongTimeoutInMillis = pingPongTimeoutInMillis;
		this.pingPong.setTimeUnitAmount(pingPongTimeoutInMillis);
	}

	@Override
	public void setPingPongResendLimit(int pingPongResendLimit) {
		this.resendLimit = pingPongResendLimit;
		this.pingPong.setResendLimit(pingPongResendLimit);
	}
	
	@Override
	public void notifyInnerConstructs(ISettings settings) {
		
	}
}
