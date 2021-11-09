package test.external.dummy;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.ServiceConnectionManager;
import test.GeneralTestUtilityClass;

public class DummyServiceConnectionManager extends ServiceConnectionManager {
	public final static long DEFAULT_PP_TIMEOUT = 200;
	public final static long DEFAULT_PP_MINIMAL_TIMEOUT = 100;
	public final static long SEND_TIMEOUT = 2000;
	public final static int RESEND_LIMIT = 10;
	
	public final static long ESTIMATED_PP_TIMEOUT = DEFAULT_PP_TIMEOUT * (RESEND_LIMIT + 1);
	
	private volatile DummyDevice currentDevice;
	private DisconnectionListener newDl = new DisconnectionListener(controller);
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es) {
//		super(manager, controller, es, 10000, 1000, 2000, 10);
		super(manager, controller, es, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}
	
	public DummyServiceConnectionManager(IDeviceManager manager, IController controller, ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(manager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	public Collection<IConnectionManager> getConnectionManagers() {
		return GeneralTestUtilityClass.getPrivateFieldValue(this, "connectionManagers");
	}
	
	public void setCurrentConnectionObject(DummyDevice currentDevice) {
		this.currentDevice = currentDevice;
		this.makeNewConnectionThread();
	}
	
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.newDl = dl;
		this.initDisconListener();
	}
	
	@Override
	protected DisconnectionListener createDisconListener() {
		return this.newDl;
	}
	
	@Override
	protected boolean addConnection(IConnection conn) {
		boolean result = super.addConnection(conn);
		System.out.println(conn.getTargetDeviceAddress() + " added");
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
		return new DummyConnectionManager(controller, conn, es, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
	}
}
