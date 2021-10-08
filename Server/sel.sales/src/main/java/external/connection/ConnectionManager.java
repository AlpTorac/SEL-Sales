package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public abstract class ConnectionManager implements IConnectionManager {
	private long minimalPingPongDelay = 1000;
	
	private long sendTimeoutInMillis = 5000;
	private long pingPongTimeoutInMillis = 10000;
	private int resendLimit = 5;
	
	private ExecutorService es;
	private IConnection conn;
	private IMessageReceptionist mr;
	private ISendBuffer sb;
	private IPingPong pingPong;
	protected IController controller;
	
	private boolean isClosed = false;
	
	private DisconnectionListener disconListener;
	
	private Thread cycleThread;
	
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
		this.init();
	}
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
	
	protected long getMinimalPingPongDelay() {
		return this.minimalPingPongDelay;
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
	}
	
	protected void checkForMessagesToBeSent() {
		this.getSendBuffer().sendMessage();
	}
	
	protected void sendPingPongMessage() {
		this.getPingPong().sendPingPongMessage();
	}
	protected void init() {
		this.initPingPong(this.getMinimalPingPongDelay(), this.getResendLimit(), this.getPingPongTimeout());
		this.initSendBuffer(this.getSendTimeout());
		this.initMessageReceptionist(this.getSendBuffer(), this.getPingPong());
		
		cycleThread = new Thread() {
			@Override
			public void run() {
				while (!isClosed && !getConnection().isClosed()) {
					checkForMessagesToBeRead();
					checkForMessagesToBeSent();
					sendPingPongMessage();
				}
			}
		};
		
		cycleThread.setDaemon(true);
		cycleThread.start();
	}
	
	protected int getResendLimit() {
		return this.resendLimit;
	}
	
	protected long getSendTimeout() {
		return this.sendTimeoutInMillis;
	}
	
	protected long getPingPongTimeout() {
		return this.pingPongTimeoutInMillis;
	}
	
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
			try {
				cycleThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
