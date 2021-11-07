package external.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.IClientManager;
import external.message.IMessage;
import model.connectivity.IClientData;
import model.settings.ISettings;
import model.settings.SettingsField;

public abstract class ServiceConnectionManager implements IServiceConnectionManager {

	private volatile boolean isClosed = false;
	
	protected ExecutorService es;
	private Collection<IConnectionManager> connectionManagers = new CopyOnWriteArrayList<IConnectionManager>();
	private IClientManager manager;
	protected IController controller;
	
	private ConnectionListener connListener;
	private DisconnectionListener disconListener;
	
	private volatile long pingPongTimeout;
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeout;
	private volatile int resendLimit;
	
//	protected ServiceConnectionManager(IClientManager manager, IController controller, ExecutorService es) {
//		this.es = es;
//		this.manager = manager;
//		this.controller = controller;
//		this.connListener = this.createConnListener();
//		this.disconListener = this.createDisconListener();
//	}
	protected ServiceConnectionManager(IClientManager manager, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.es = es;
		this.manager = manager;
		this.controller = controller;
//		this.connListener = this.createConnListener();
//		this.disconListener = this.createDisconListener();
		this.initConnListener();
		this.initDisconListener();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
	}

	protected void initConnListener() {
		this.connListener = this.createConnListener();
	}
	
	protected void initDisconListener() {
		this.disconListener = this.createDisconListener();
	}
	
	@Override
	public void notifyInnerConstructs(ISettings settings) {
		this.connectionManagers.forEach(cm -> cm.receiveSettings(settings));
	}
	
	@Override
	public long getSendTimeoutInMillis() {
		return this.sendTimeout;
	}

	@Override
	public long getPingPongTimeoutInMillis() {
		return this.pingPongTimeout;
	}

	@Override
	public int getPingPongResendLimit() {
		return this.resendLimit;
	}
	
	@Override
	public long getMinimalPingPongDelay() {
		return this.minimalPingPongDelay;
	}
	
//	protected long getPingPongTimeout() {
//		return this.pingPongTimeout;
//	}
//	
//	protected long getSendTimeout() {
//		return this.sendTimeout;
//	}
//	
//	protected int getResendLimit() {
//		return this.resendLimit;
//	}
//	
//	protected long getMinimalPingPongDelay() {
//		return this.minimalPingPongDelay;
//	}
	
	@Override
	public void setMinimalPingPongDelay(long minimalPingPongDelay) {
		this.minimalPingPongDelay = minimalPingPongDelay;
	}

	@Override
	public void setSendTimeoutInMillis(long sendTimeoutInMillis) {
		this.sendTimeout = sendTimeoutInMillis;
	}

	@Override
	public void setPingPongTimeoutInMillis(long pingPongTimeoutInMillis) {
		this.pingPongTimeout = pingPongTimeoutInMillis;
	}

	@Override
	public void setPingPongResendLimit(int pingPongResendLimit) {
		this.resendLimit = pingPongResendLimit;
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
	
	protected void reportConnection(IConnection conn) {
		this.connListener.connectionEstablished(conn.getTargetClientAddress());
	}
	
	protected boolean addConnection(IConnection conn) {
		if (this.isConnectionAllowed(conn.getTargetClientAddress())) {
			System.out.println("Connection added");
//			IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeout(), this.getSendTimeout(), this.getResendLimit(), this.getMinimalPingPongDelay());
			IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
//			this.connListener.connectionEstablished(conn.getTargetClientAddress());
			this.reportConnection(conn);
			connManager.setDisconnectionListener(this.disconListener);
			System.out.println("Connection manager added");
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
		if (!this.isClosed()) {
			this.acceptIncomingConnection();
		}
	}
	@Override
	public void close() {
		this.isClosed = true;
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
//		Collection<IConnectionManager> closedCMs = new ArrayList<IConnectionManager>();
		for (IClientData d : clientData) {
			this.connectionManagers.stream()
			.filter(cm -> !cm.isClosed())
			.filter(cm -> cm.getConnection().getTargetClientAddress().equals(d.getClientAddress()))
			.forEach(cm -> {
				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
					cm.close();
					this.connectionManagers.remove(cm);
					this.disconListener.connectionLost(d.getClientAddress());
//					closedCMs.add(cm);
				}
			});
		}
//		this.connectionManagers.removeAll(closedCMs);
//		this.connectionManagers.stream().filter(cm -> cm.getConnection().isClosed()).forEach(cm -> {
//
//		});
	}
	@Override
	public boolean isClosed() {
		return this.isClosed;
	}
	protected ConnectionListener createConnListener() {
		return new ConnectionListener(this.controller);
	}

	protected DisconnectionListener createDisconListener() {
		return new DisconnectionListener(this.controller);
	}
}