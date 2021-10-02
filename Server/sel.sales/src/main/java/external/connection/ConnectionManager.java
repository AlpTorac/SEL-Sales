package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public abstract class ConnectionManager implements IConnectionManager {
	private ExecutorService es;
	private IConnection conn;
	private IMessageReceptionist iml;
	private ISendBuffer sb;
	private IPingPong pingPong;
	protected IController controller;
	
	private ConnectionListener connListener;
	private DisconnectionListener disconListener;
	
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
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
	
	protected abstract ConnectionListener initConnListener();
	
	protected abstract DisconnectionListener initDisconListener();
	
	protected abstract IPingPong initPingPong();
	
	protected abstract ISendBuffer initSendBuffer();
	
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
				while (!getConnection().isClosed()) {
					getIncomingMessageListener().checkForMessages();
				}
			}
		};
	}
	
	protected Runnable initMessageSendingRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				while (!getConnection().isClosed()) {
					if (!getSendBuffer().isBlocked() && !getSendBuffer().isEmpty()) {
						getSendBuffer().sendMessage();
					}
				}
			}
		};
	}
	
	protected Runnable initPingPongRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				getPingPong().start();
				while (!getConnection().isClosed() && getPingPong().isRunning()) {
					
				}
				disconListener.connectionLost(conn.getTargetClientAddress());
			}
		};
	}
	
	protected void init() {
		ISendBuffer sendBuffer = initSendBuffer();
		setSendBuffer(sendBuffer);
		IMessageReceptionist incomingMessageListener = initIncomingMessageListener();
		setIncomingMessageListener(incomingMessageListener);
		IPingPong pingPong = initPingPong();
		setPingPong(pingPong);
		this.connListener = initConnListener();
		this.connListener.connectionEstablished(this.conn.getTargetClientAddress());
		this.disconListener = initDisconListener();
		Runnable[] rs = this.initConnectionRunnables();
		for (Runnable r : rs) {
			getExecutorService().submit(r);
		}
	}
	
	@Override
	public void sendMessage(IMessage message) {
		getSendBuffer().addMessage(message);
	}
	
	@Override
	public void close() {
		try {
			getSendBuffer().close();
			getConnection().close();
			getIncomingMessageListener().close();
			getPingPong().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
