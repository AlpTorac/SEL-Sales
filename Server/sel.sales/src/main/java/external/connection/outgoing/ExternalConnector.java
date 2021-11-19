package external.connection.outgoing;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.ConnectionListener;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.device.IDevice;
import external.message.IMessage;
import model.connectivity.IDeviceData;
import model.settings.ISettings;

public abstract class ExternalConnector implements IExternalConnector {
	
	private Collection<String> activeConnRunnables;
	
	private volatile boolean hasRunningRunnable = false;
	private volatile boolean isClosed = false;
	
	protected ExecutorService es;
	private Collection<IConnectionManager> connectionManagers;
	private IService service;
	protected IController controller;
	
	private ConnectionListener connListener;
	private DisconnectionListener disconListener;
	
	private volatile long pingPongTimeout;
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeout;
	private volatile int resendLimit;
	
	protected ExternalConnector(IService service, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.connectionManagers = new CopyOnWriteArrayList<IConnectionManager>();
		this.activeConnRunnables = new CopyOnWriteArrayList<String>();
		this.es = es;
		this.service = service;
		this.controller = controller;
		this.initConnListener();
		this.initDisconListener();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
	}

	protected IService getService() {
		return this.service;
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
	
	private IConnectionManager getConnectionManager(String deviceAddress) {
		Iterator<IConnectionManager> it = this.connectionManagers.iterator();
		while (it.hasNext()) {
			IConnectionManager current = it.next();
			if (current.getConnection().getTargetDeviceAddress().equals(deviceAddress)) {
				return current;
			}
		}
		return null;
	}
	
	@Override
	public IConnection getConnection(String deviceAddress) {
		IConnectionManager cm = this.getConnectionManager(deviceAddress);
		if (cm != null) {
			return cm.getConnection();
		}
		return null;
	}
	/**
	 * @param connectionObject The object that contains the information about the connection (not the {@link IConnection} instance)
	 */
	protected abstract IConnection initConnection(Object connectionObject);
	
	protected abstract Object getConnectionObject(Object serviceID, IDevice serviceHost);
	
	protected abstract IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay);
	
	protected void reportConnection(IConnection conn) {
		this.connListener.connectionEstablished(conn.getTargetDeviceAddress());
	}
	
	protected void reportDisconnection(IConnection conn) {
		this.disconListener.connectionLost(conn.getTargetDeviceAddress());
//		if (this.getService().getDeviceManager().isAllowedToConnect(conn.getTargetDeviceAddress())) {
//			final Object o = new Object();
//			
//			while (!this.getService().getDeviceManager().isAllowedToConnect(conn.getTargetDeviceAddress())) {
//				synchronized (o) {
//					this.connectToService(this.getService().getID(), conn.getTargetDeviceAddress());
//				}
//				try {
//					o.wait(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	protected boolean addConnection(IConnection conn) {
		System.out.println("Connection added");
//		IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeout(), this.getSendTimeout(), this.getResendLimit(), this.getMinimalPingPongDelay());
		IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
//		this.connListener.connectionEstablished(conn.getTargetDeviceAddress());
		this.reportConnection(conn);
		connManager.setDisconnectionListener(this.disconListener);
		System.out.println("Connection manager added");
		return this.connectionManagers.add(connManager);
	}
	
	protected Runnable initConnectionRunnable(Object serviceID, IDevice serviceHost) {
		return new Runnable() {
			@Override
			public void run() {
				while (hasRunningRunnable) {
					
				}
				hasRunningRunnable = true;
				IConnection conn = initConnection(getConnectionObject(serviceID, serviceHost));
				if (!addConnection(conn)) {
					return;
				}
				hasRunningRunnable = false;
				activeConnRunnables.remove(serviceHost.getDeviceAddress());
//				IConnection conn = initConnection(getConnectionObject(serviceID, serviceHost));
//				if (!addConnection(conn)) {
//					return;
//				}
			}
		};
	}
	
	@Override
	public void close() {
		this.isClosed = true;
		this.es.shutdown();
		this.connectionManagers.forEach(cm -> cm.close());
		this.es = null;
	}
	@Override
	public void sendMessageTo(String deviceAddress, IMessage message) {
		this.getConnectionManager(deviceAddress).sendMessage(message);
	}
	@Override
	public void broadcastMessage(IMessage message) {
		this.connectionManagers.forEach(cm -> cm.sendMessage(message));
	}
	@Override
	public void receiveKnownDeviceData(IDeviceData[] deviceData) {
		for (IDeviceData d : deviceData) {
			IConnectionManager cm = this.getConnectionManager(d.getDeviceAddress());
			if (cm != null && !cm.isClosed()) {
				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
					cm.close();
					this.connectionManagers.remove(cm);
					reportDisconnection(cm.getConnection());
				}
			} else if (d.getIsAllowedToConnect() && !d.getIsConnected()) {
				this.connectToKnownDevice(d);
			}
//			this.connectionManagers.stream()
//			.filter(cm -> !cm.isClosed())
//			.filter(cm -> cm.getConnection().getTargetDeviceAddress().equals(d.getDeviceAddress()))
//			.forEach(cm -> {
//				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
//					cm.close();
//					this.connectionManagers.remove(cm);
//					reportDisconnection(cm.getConnection());
////					this.disconListener.connectionLost(d.getDeviceAddress());
//				}
//			});
		}
	}
	
	protected void connectToKnownDevice(IDeviceData d) {
		this.connectToService(this.getService().getID(), d.getDeviceAddress());
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
	
	protected abstract String getConnectionAddress(Object serviceID, IDevice serviceHost);
	
	@Override
	public void connectToService(Object serviceID, String serviceHostAddress) {
		if (this.getService().getDeviceManager().isAllowedToConnect(serviceHostAddress) && !this.activeConnRunnables.contains(serviceHostAddress)) {
			this.activeConnRunnables.add(serviceHostAddress);
			this.es.submit(this.initConnectionRunnable(serviceID, this.getService().getDeviceManager().getDevice(serviceHostAddress)));
			System.out.println("Connection runnable submitted");
		}
//		this.es.submit(this.initConnectionRunnable(serviceID, this.getService().getDeviceManager().getDevice(serviceHostAddress)));
	}
}
