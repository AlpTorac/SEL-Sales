package test.external.dummy;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.connection.outgoing.ExternalConnector;
import external.device.IDevice;
import model.connectivity.IDeviceData;
import test.GeneralTestUtilityClass;

public class DummyExternalConnector extends ExternalConnector {

	public final static long DEFAULT_PP_TIMEOUT = 200;
	public final static long DEFAULT_PP_MINIMAL_TIMEOUT = 100;
	public final static long SEND_TIMEOUT = 2000;
	public final static int RESEND_LIMIT = 10;
	
	public final static long ESTIMATED_PP_TIMEOUT = DEFAULT_PP_TIMEOUT * (RESEND_LIMIT + 1);
	
	private volatile DummyDevice currentDevice;
	private DisconnectionListener newDl = new DisconnectionListener(controller);
	
	public DummyExternalConnector(IService service, IController controller, ExecutorService es, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(service, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	public DummyExternalConnector(IService service, IController controller, ExecutorService es) {
		this(service, controller, es, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}

	public Collection<IConnectionManager> getConnectionManagers() {
		return GeneralTestUtilityClass.getPrivateFieldValue(this, "connectionManagers");
	}
	
	public void setCurrentConnectionObject(DummyDevice currentDevice) {
		if (!this.getConnectionManagers().stream().anyMatch(cm -> cm.getConnection().getTargetDeviceAddress().equals(currentDevice.getDeviceAddress()))) {
			this.currentDevice = currentDevice;
			this.connectToService(null, this.currentDevice.getDeviceAddress());
		}
	}
	
	public void setDisconnectionListener(DisconnectionListener dl) {
		this.newDl = dl;
		this.initDisconListener();
	}
	
	@Override
	protected void connectToKnownDevice(IDeviceData d) {
		this.setCurrentConnectionObject((DummyDevice) this.getService().getDeviceManager().getDevice(d.getDeviceAddress()));
	}
	
	@Override
	protected DisconnectionListener createDisconListener() {
		return this.newDl;
	}
	
	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new DummyConnection(((DummyDevice) getConnectionObject(null, null)).getDeviceAddress());
	}

	@Override
	protected Object getConnectionObject(Object serviceID, IDevice serviceHost) {
		return currentDevice;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout,
			int resendLimit, long minimalPingPongDelay) {
		return new DummyConnectionManager(controller, conn, es, this.getPingPongTimeoutInMillis(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit(), this.getMinimalPingPongDelay());
	}

	@Override
	protected String getConnectionAddress(Object serviceID, IDevice serviceHost) {
		// TODO Auto-generated method stub
		return null;
	}

}
