package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import model.connectivity.IDeviceData;

public abstract class ServiceConnectionManager extends ConnectionContainer {

	private IDeviceManager manager;
	
	protected ServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, es, sendTimeout, sendTimeout, sendTimeout, resendLimit);
		this.manager = manager;
	}
	
	protected IDeviceManager getDeviceManager() {
		return this.manager;
	}
	
	/**
	 * @param connectionObject The object that contains the information about the connection (not the {@link IConnection} instance)
	 */
	protected abstract IConnection initConnection(Object connectionObject);
	
	protected abstract Object getConnectionObject();
	
	protected boolean addConnection(IConnection conn) {
		if (this.isConnectionAllowed(conn.getTargetDeviceAddress())) {
			System.out.println("Connection added");
			IConnectionManager connManager = this.createConnectionManager(conn, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
			this.reportConnection(conn);
			connManager.setDisconnectionListener(this.getDisconnectionListener());
			System.out.println("Connection manager added");
			return this.getConnectionManagers().add(connManager);
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
		this.getES().submit(this.initConnectionRunnable());
	}

	public boolean isConnectionAllowed(String deviceAddress) {
		return this.manager.isAllowedToConnect(deviceAddress);
	}
	
	public void makeNewConnectionThread() {
		if (!this.isClosed()) {
			this.acceptIncomingConnection();
		}
	}
	@Override
	public void receiveKnownDeviceData(IDeviceData[] deviceData) {
		for (IDeviceData d : deviceData) {
			this.getConnectionManagers().stream()
			.filter(cm -> !cm.isClosed())
			.filter(cm -> cm.getConnection().getTargetDeviceAddress().equals(d.getDeviceAddress()))
			.forEach(cm -> {
				if (!d.getIsAllowedToConnect() || !d.getIsConnected()) {
					cm.close();
					this.getConnectionManagers().remove(cm);
					this.getDisconnectionListener().connectionLost(d.getDeviceAddress());
				}
			});
			if (d.getIsAllowedToConnect() && !d.getIsConnected()) {
				connectionAlgorithm(d);
			}
		}
	}
	
	protected void connectionAlgorithm(IDeviceData d) {
		
	}
}