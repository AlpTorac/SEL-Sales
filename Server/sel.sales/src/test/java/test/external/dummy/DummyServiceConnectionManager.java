package test.external.dummy;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import model.connectivity.IDeviceData;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.ServiceConnectionManager;
import test.GeneralTestUtilityClass;

public class DummyServiceConnectionManager extends ServiceConnectionManager {
	private boolean attemptToReconnect = false;
	
	private volatile DummyDevice currentDevice;
	private DisconnectionListener newDl = new DisconnectionListener(this.getController());
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es, boolean attemptToReconnect) {
//		super(manager, controller, es, 10000, 1000, 2000, 10);
		this(manager, controller, es,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
		this.attemptToReconnect = attemptToReconnect;
	}
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es) {
//		super(manager, controller, es, 10000, 1000, 2000, 10);
		this(manager, controller, es,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
	}
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(manager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setDisconnectionListener(new DisconnectionListener(controller));
	}
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit, boolean attemptToReconnect) {
		this(manager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.attemptToReconnect = attemptToReconnect;
	}
	
	public Collection<IConnectionManager> getConnectionManagers() {
		return GeneralTestUtilityClass.getPrivateFieldValue(this, "connectionManagers");
	}
	
	public void setCurrentConnectionObject(DummyDevice currentDevice) {
		if (!this.getConnectionManagers().stream().anyMatch(cm -> cm.getConnection().getTargetDeviceAddress().equals(currentDevice.getDeviceAddress()))) {
			this.currentDevice = currentDevice;
			this.makeNewConnectionThread();
		}

	}
	
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.newDl = dl;
		this.initDisconListener();
	}
	
	@Override
	protected void connectionAlgorithm(IDeviceData d) {
		if (this.attemptToReconnect) {
			System.out.println("Set connection target: " + d.getDeviceAddress() + " -----------------------------------------");
			this.setCurrentConnectionObject((DummyDevice) this.getDeviceManager().getDevice(d.getDeviceAddress()));
		}
	}
	
	@Override
	protected DisconnectionListener createDisconListener() {
		return this.newDl;
	}
	
	@Override
	protected boolean addConnection(IConnection conn) {
		boolean result = super.addConnection(conn);
		System.out.println(conn.getTargetDeviceAddress() + " added---------------------------------------------------");
		return result;
	}
	
	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new DummyConnection(((DummyDevice) getConnectionObject()).getDeviceAddress());
	}

	@Override
	protected Object getConnectionObject() {
		return currentDevice;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay) {
//		return new DummyConnectionManager(controller, conn, es, this.getPingPongTimeout(), this.getSendTimeout(), this.getResendLimit(), this.getMinimalPingPongDelay());
		return new DummyConnectionManager(this.getController(), conn, this.getES(), this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
	}
}
