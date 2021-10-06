package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public abstract class ConnectionManager implements IConnectionManager {
	private long sendTimeoutInMillis = 5000;
	private long pingPongTimeoutInMillis = 1000;
	private int resendLimit = 10;
	
	private ExecutorService es;
	private IConnection conn;
	private IMessageReceptionist mr;
	private ISendBuffer sb;
	private IPingPong pingPong;
	protected IController controller;
	
	private boolean isClosed = false;
	
	private ConnectionListener connListener;
	private DisconnectionListener disconListener;
	
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
		this.init();
	}
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es, long pingPongTimeoutInMillis, long sendTimeoutInMillis, int resendLimit) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
		this.pingPongTimeoutInMillis = pingPongTimeoutInMillis;
		this.sendTimeoutInMillis = sendTimeoutInMillis;
		this.resendLimit = resendLimit;
		this.init();
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
	
	protected void initConnListener() {
		this.connListener = this.createConnListener();
		this.connListener.connectionEstablished(this.conn.getTargetClientAddress());
	}
	
	protected abstract ConnectionListener createConnListener();
	
	protected abstract DisconnectionListener createDisconListener();
	
	protected void setDisconListener(DisconnectionListener dl) {
		this.disconListener = dl;
		this.pingPong.setDisconnectionListener(this.disconListener);
	}
	
	protected void initDisconListener(IPingPong pingPong) {
		this.setDisconListener(this.createDisconListener());
	}
	
	protected void initPingPong(int resendLimit, long pingPongTimeoutInMillis) {
		this.pingPong = this.createPingPong(resendLimit, pingPongTimeoutInMillis);
	}

	protected abstract IPingPong createPingPong(int resendLimit, long pingPongTimeoutInMillis);
	
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
	
	protected abstract Runnable[] initConnectionRunnables();
	
	protected Runnable initMessageReadingRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				while (!isClosed && !getConnection().isClosed()) {
//					System.out.println("Checking for messages");
					getIncomingMessageListener().checkForMessages();
//					System.out.println("Checked for messages");
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
			}
		};
	}
	
	protected Runnable initMessageSendingRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				while (!isClosed && !getConnection().isClosed()) {
//					System.out.println("Checking for messages to send");
					if (!getSendBuffer().isBlocked() && !getSendBuffer().isEmpty()) {
						getSendBuffer().sendMessage();
					}
//					System.out.println("Checked for messages to send");
//					try {
//						Thread.sleep(300);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
			}
		};
	}
	
	protected Runnable initPingPongRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				while (!isClosed && !getConnection().isClosed()) {
					if (!getPingPong().isBlocked()) {
						getPingPong().sendPingPongMessage();
					}
				}
			}
		};
	}
	
	protected void init() {
		this.initPingPong(this.getResendLimit(), this.getPingPongTimeout());

		this.initSendBuffer(this.getSendTimeout());

		this.initMessageReceptionist(this.getSendBuffer(), this.getPingPong());
		
		this.initConnListener();
		this.initDisconListener(this.getPingPong());
		Runnable[] rs = this.initConnectionRunnables();
		for (Runnable r : rs) {
			this.getExecutorService().submit(r);
		}
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
		isClosed = true;
		try {
			this.getSendBuffer().close();
			this.getPingPong().close();
			this.getIncomingMessageListener().close();
			this.getConnection().close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
}
