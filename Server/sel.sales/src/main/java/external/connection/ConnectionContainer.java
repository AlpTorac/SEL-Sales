package external.connection;

import java.io.Closeable;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.message.IMessage;
import model.connectivity.IDeviceData;
import model.settings.ISettings;

public abstract class ConnectionContainer implements Closeable, IHasConnectionSettings {

	private volatile boolean isClosed = false;

	private ExecutorService es;
	private Collection<IConnectionManager> connectionManagers;
	private IController controller;

	private ConnectionListener connListener;
	private DisconnectionListener disconListener;

	private volatile long pingPongTimeout;
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeout;
	private volatile int resendLimit;

	protected ConnectionContainer(IController controller, ExecutorService es, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.connectionManagers = new CopyOnWriteArrayList<IConnectionManager>();
		this.es = es;
		this.controller = controller;
		this.initConnListener();
		this.initDisconListener();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.init();
	}
	
	protected void init() {
		this.getES().submit(()->{
			while (!this.isClosed()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.getConnectionManagers().forEach(cm -> {
					if (!cm.isClosed() && !cm.getConnection().isClosed()) {
						cm.checkCycle();
					}
				});
			}
		});
	}
	
	protected abstract IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay);
	
	protected Collection<IConnectionManager> getConnectionManagers() {
		return this.connectionManagers;
	}

	protected IConnectionManager getConnectionManager(String deviceAddress) {
		Iterator<IConnectionManager> it = this.getConnectionManagers().iterator();
		while (it.hasNext()) {
			IConnectionManager current = it.next();
			if (current.getConnection().getTargetDeviceAddress().equals(deviceAddress)) {
				return current;
			}
		}
		return null;
	}

	protected ExecutorService getES() {
		return this.es;
	}

	protected IController getController() {
		return this.controller;
	}

	protected void initConnListener() {
		this.setConnectionListener(this.createConnListener());
	}

	protected void initDisconListener() {
		this.getDisconnectionListener(this.createDisconListener());
	}

	protected void reportConnection(IConnection conn) {
		this.getConnectionListener().connectionEstablished(conn.getTargetDeviceAddress());
	}

	protected void reportDisconnection(IConnection conn) {
		this.getDisconnectionListener().connectionLost(conn.getTargetDeviceAddress());
	}

	protected ConnectionListener createConnListener() {
		return new ConnectionListener(this.controller);
	}

	protected DisconnectionListener createDisconListener() {
		return new DisconnectionListener(this.controller);
	}
	
	protected void setConnectionListener(ConnectionListener l) {
		this.connListener = l;
	}
	
	protected void getDisconnectionListener(DisconnectionListener l) {
		this.disconListener = l;
		for (IConnectionManager cm : this.getConnectionManagers()) {
			cm.setDisconnectionListener(l);
		}
	}
	
	protected ConnectionListener getConnectionListener() {
		return this.connListener;
	}
	
	protected DisconnectionListener getDisconnectionListener() {
		return this.disconListener;
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

	@Override
	public void notifyInnerConstructs(ISettings settings) {
		this.getConnectionManagers().forEach(cm -> cm.receiveSettings(settings));
	}

	public IConnection getConnection(String deviceAddress) {
		IConnectionManager cm = this.getConnectionManager(deviceAddress);
		if (cm != null) {
			return cm.getConnection();
		}
		return null;
	}

	public boolean isClosed() {
		return this.isClosed;
	}

	public void close() {
		this.isClosed = true;
//		this.getES().shutdown();
		this.getConnectionManagers().forEach(cm -> cm.close());
	}

	public void sendMessageTo(String deviceAddress, IMessage message) {
		this.getConnectionManager(deviceAddress).sendMessage(message);
	}

	public void broadcastMessage(IMessage message) {
		this.getConnectionManagers().forEach(cm -> cm.sendMessage(message));
	}

	public abstract void receiveKnownDeviceData(IDeviceData[] deviceData);
}
