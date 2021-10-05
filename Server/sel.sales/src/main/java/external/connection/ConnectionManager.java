package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public abstract class ConnectionManager implements IConnectionManager {
	private long sendTimeoutInMillis;
	private long pingPongTimeoutInMillis;
	private int resendLimit;
	
	private ExecutorService es;
	private IConnection conn;
	private IMessageReceptionist iml;
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
		this(controller, conn, es);
		this.pingPongTimeoutInMillis = pingPongTimeoutInMillis;
		this.sendTimeoutInMillis = sendTimeoutInMillis;
		this.resendLimit = resendLimit;
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
	
	protected abstract ConnectionListener initConnListener();
	
	protected abstract DisconnectionListener initDisconListener();
	
	protected abstract IPingPong initPingPong(int resendLimit, long pingPongTimeoutInMillis);

	protected abstract ISendBuffer initSendBuffer(long timeoutInMillis);
	
	protected abstract IMessageReceptionist initIncomingMessageListener();
	
	private void setIncomingMessageListener(IMessageReceptionist iml) {
		this.iml = iml;
	}
	
	private void setSendBuffer(ISendBuffer sb) {
		this.sb = sb;
	}
	
	private void setPingPong(IPingPong pingPong) {
		this.pingPong = pingPong;
	}
	
	@Override
	public ISendBuffer getSendBuffer() {
		return this.sb;
	}
	@Override
	public IMessageReceptionist getIncomingMessageListener() {
		return this.iml;
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
				getPingPong().start();
				while (!isClosed && !getConnection().isClosed() && getPingPong().isRunning()) {
//					System.out.println("Checking for ping pong");
//					try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				disconListener.connectionLost(conn.getTargetClientAddress());
			}
		};
	}
	
	protected void init() {
		ISendBuffer sendBuffer = initSendBuffer(this.getSendTimeout());
		setSendBuffer(sendBuffer);
		IMessageReceptionist incomingMessageListener = initIncomingMessageListener();
		setIncomingMessageListener(incomingMessageListener);
		IPingPong pingPong = initPingPong(this.getResendLimit(), this.getPingPongTimeout());
		setPingPong(pingPong);
		this.connListener = initConnListener();
		this.connListener.connectionEstablished(this.conn.getTargetClientAddress());
		this.disconListener = initDisconListener();
		Runnable[] rs = this.initConnectionRunnables();
		for (Runnable r : rs) {
			getExecutorService().submit(r);
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
		getSendBuffer().addMessage(message);
	}
	
	@Override
	public void close() {
		isClosed = true;
		try {
			getConnection().close();
			getSendBuffer().close();
			getIncomingMessageListener().close();
			getPingPong().close();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
}
