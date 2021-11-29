package test.external.dummy;

import java.util.concurrent.ExecutorService;

import external.IConnectionUtility;
import external.connection.IConnection;
import external.device.DeviceDiscoveryStrategy;
import server.controller.IServerController;
import server.external.StandardServerExternal;
import server.model.IServerModel;

public class DummyStandardServerExternal extends StandardServerExternal implements IDummyExternal {
	public DummyStandardServerExternal(IServerController controller, IServerModel model, IConnectionUtility connUtil,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, connUtil, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}

	public DummyStandardServerExternal(IServerController controller, IServerModel model, IConnectionUtility connUtil) {
		this(controller, model, connUtil, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}
	
	public ExecutorService getES() {
		return this.es;
	}
	
	public IConnection getConnection(String deviceAddress) {
//		return this.getService().getServiceConnectionManager().getConnection(deviceAddress);
		return this.getConnectionUtility().getConnection(deviceAddress);
	}

	protected DummyConnectionUtility getConnectionUtility() {
		return (DummyConnectionUtility) super.getConnectionUtility();
	}
	
	public void setDiscoveryStrategy(DeviceDiscoveryStrategy cds) {
		System.out.println("Discovery strategy set");
		this.getService().getDeviceManager().setDiscoveryStrategy(cds);
	}

}
