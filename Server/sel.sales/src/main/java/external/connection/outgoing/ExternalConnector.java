package external.connection.outgoing;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.ConnectionContainer;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.device.IDevice;
import model.connectivity.IDeviceData;

public abstract class ExternalConnector extends ConnectionContainer {
	private Collection<String> activeConnRunnables;
	private volatile boolean hasRunningRunnable = false;
	private IService service;
	
	protected ExternalConnector(IService service, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, es, sendTimeout, sendTimeout, sendTimeout, resendLimit);
		this.activeConnRunnables = new CopyOnWriteArrayList<String>();
		this.service = service;
	}
	
	protected IService getService() {
		return this.service;
	}
	
	protected void reportDisconnection(IConnection conn) {
		super.reportDisconnection(conn);
		this.activeConnRunnables.remove(conn.getTargetDeviceAddress());
	}
	
	/**
	 * @param connectionObject The object that contains the information about the connection (not the {@link IConnection} instance)
	 */
	protected abstract IConnection initConnection(Object connectionObject);
	
	protected abstract Object getConnectionObject(Object serviceID, IDevice serviceHost);
	
	protected boolean addConnection(IConnection conn) {
		System.out.println("Connection added: " + conn.getTargetDeviceAddress());
//		IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeout(), this.getSendTimeout(), this.getResendLimit(), this.getMinimalPingPongDelay());
		IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
//		this.connListener.connectionEstablished(conn.getTargetDeviceAddress());
		this.reportConnection(conn);
		connManager.setDisconnectionListener(this.getDisconnectionListener());
		System.out.println("Connection manager added");
		return this.getConnectionManagers().add(connManager);
	}
	
	protected Runnable initConnectionRunnable(Object serviceID, IDevice serviceHost) {
		return new Runnable() {
			@Override
			public void run() {
				while (hasRunningRunnable) {
					
				}
				hasRunningRunnable = true;
				IConnection conn = initConnection(getConnectionObject(serviceID, serviceHost));
				System.out.println("Adding connection: " + conn.getTargetDeviceAddress());
				if (!addConnection(conn)) {
					return;
				}
				hasRunningRunnable = false;
				activeConnRunnables.remove(serviceHost.getDeviceAddress());
			}
		};
	}
	
	@Override
	public void receiveKnownDeviceData(IDeviceData[] deviceData) {
		for (IDeviceData d : deviceData) {
			IConnectionManager cm = this.getConnectionManager(d.getDeviceAddress());
			if (cm != null && !cm.isClosed()) {
				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
					cm.close();
					this.getConnectionManagers().remove(cm);
					reportDisconnection(cm.getConnection());
				}
			} else if (d.getIsAllowedToConnect() && !d.getIsConnected()) {
				this.connectToKnownDevice(d);
			}
		}
	}
	
	protected void connectToKnownDevice(IDeviceData d) {
		this.connectToService(this.getService().getID(), d.getDeviceAddress());
	}
	
	protected abstract String getConnectionAddress(Object serviceID, IDevice serviceHost);
	
	public void connectToService(Object serviceID, String serviceHostAddress) {
		if (this.getService().getDeviceManager().isAllowedToConnect(serviceHostAddress) && !this.activeConnRunnables.contains(serviceHostAddress)) {
			this.activeConnRunnables.add(serviceHostAddress);
			this.getES().submit(this.initConnectionRunnable(serviceID, this.getService().getDeviceManager().getDevice(serviceHostAddress)));
			System.out.println("Connection runnable submitted");
		}
	}
}
