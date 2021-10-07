package external.connection;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.IClientManager;
import external.message.IMessage;
import model.connectivity.IClientData;

public abstract class ServiceConnectionManager implements IServiceConnectionManager {

	protected ExecutorService es;
	private Collection<IConnectionManager> connectionManagers = new CopyOnWriteArrayList<IConnectionManager>();
	private IClientManager manager;
	protected IController controller;
	
	private ConnectionListener connListener;
	private DisconnectionListener disconListener;
	
	private long pingPongTimeout = 10000;
	private long minimalPingPongDelay = 1000;
	private long sendTimeout = 5000;
	private int resendLimit = 5;
	
	protected ServiceConnectionManager(IClientManager manager, IController controller, ExecutorService es) {
		this.es = es;
		this.manager = manager;
		this.controller = controller;
		this.connListener = this.createConnListener();
		this.disconListener = this.createDisconListener();
	}
	protected ServiceConnectionManager(IClientManager manager, IController controller, ExecutorService es, long pingPongTimeout, long sendTimeout, int resendLimit) {
		this(manager, controller, es);
		this.pingPongTimeout = pingPongTimeout;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
	}

	protected void initConnListener() {
		this.connListener = this.createConnListener();
	}
	
	protected void setPingPongTimeout(long pingPongTimeout) {
		this.pingPongTimeout = pingPongTimeout;
	}
	
	protected void setSendTimeout(long sendTimeout) {
		this.sendTimeout = sendTimeout;
	}
	
	protected void setResendLimit(int resendLimit) {
		this.resendLimit = resendLimit;
	}
	
	protected long getPingPongTimeout() {
		return this.pingPongTimeout;
	}
	
	protected long getSendTimeout() {
		return this.sendTimeout;
	}
	
	protected int getResendLimit() {
		return this.resendLimit;
	}
	
	protected long getMinimalPingPongDelay() {
		return this.minimalPingPongDelay;
	}
	
	private IConnectionManager getConnectionManager(String clientAddress) {
		Iterator<IConnectionManager> it = this.connectionManagers.iterator();
		while (it.hasNext()) {
			IConnectionManager current = it.next();
			if (current.getConnection().getTargetClientAddress().equals(clientAddress)) {
				return current;
			}
		}
		return null;
	}
	
	@Override
	public IConnection getConnection(String clientAddress) {
		IConnectionManager cm = this.getConnectionManager(clientAddress);
		if (cm != null) {
			return cm.getConnection();
		}
		return null;
	}
	/**
	 * @param connectionObject The object that contains the information about the connection (not the {@link IConnection} instance)
	 */
	protected abstract IConnection initConnection(Object connectionObject);
	
	protected abstract Object getConnectionObject();
	
	protected abstract IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay);
	
	protected boolean addConnection(IConnection conn) {
		if (this.isConnectionAllowed(conn.getTargetClientAddress())) {
			IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeout(), this.getSendTimeout(), this.getResendLimit(), this.getMinimalPingPongDelay());
			this.connListener.connectionEstablished(conn.getTargetClientAddress());
			connManager.setDisconnectionListener(this.disconListener);
			return this.connectionManagers.add(connManager);
		} else {
			try {
				conn.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
			return false;
		}
	}
	
	protected Runnable initConnectionRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				IConnection conn = initConnection(getConnectionObject());
				if (!addConnection(conn)) {
					return;
				}
			}
		};
	}
	
	protected void acceptIncomingConnection() {
		this.es.submit(this.initConnectionRunnable());
	}

	@Override
	public boolean isConnectionAllowed(String clientAddress) {
		return this.manager.isAllowedToConnect(clientAddress);
	}
	@Override
	public void makeNewConnectionThread() {
		this.acceptIncomingConnection();
	}
	@Override
	public void close() {
		this.es.shutdown();
		this.connectionManagers.forEach(cm -> cm.close());
		this.es = null;
		this.manager = null;
	}
	@Override
	public void sendMessageTo(String clientAddress, IMessage message) {
		this.getConnectionManager(clientAddress).sendMessage(message);
	}
	@Override
	public void broadcastMessage(IMessage message) {
		this.connectionManagers.forEach(cm -> cm.sendMessage(message));
	}
	@Override
	public void receiveKnownClientData(IClientData[] clientData) {
		for (IClientData d : clientData) {
			this.connectionManagers.stream()
			.filter(cm -> cm.getConnection().getTargetClientAddress().equals(d.getClientAddress()))
			.forEach(cm -> {
				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
					cm.close();
					this.connectionManagers.remove(cm);
					this.disconListener.connectionLost(cm.getConnection().getTargetClientAddress());
				}
			});
		}
//		this.connectionManagers.stream().filter(cm -> cm.getConnection().isClosed()).forEach(cm -> {
//
//		});
	}
	
	protected ConnectionListener createConnListener() {
		return new ConnectionListener(this.controller);
	}

	protected DisconnectionListener createDisconListener() {
		return new DisconnectionListener(this.controller);
	}
}