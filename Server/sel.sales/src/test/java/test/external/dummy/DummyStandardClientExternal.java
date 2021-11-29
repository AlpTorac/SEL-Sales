package test.external.dummy;

import java.util.concurrent.ExecutorService;

import client.controller.IClientController;
import client.external.StandardClientExternal;
import client.model.IClientModel;
import external.IConnectionUtility;
import external.connection.IConnection;
import external.connection.IService;
import external.connection.outgoing.IExternalConnector;
import external.device.DeviceDiscoveryStrategy;
import external.device.IDeviceManager;

public class DummyStandardClientExternal extends StandardClientExternal implements IDummyExternal {
	protected DummyStandardClientExternal(IClientController controller, IClientModel model, IConnectionUtility connUtil, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, connUtil, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		System.out.println("Service: " + this.getService());
	}

	public DummyStandardClientExternal(IClientController controller, IClientModel model, IConnectionUtility connUtil) {
		this(controller, model, connUtil, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}
	
	public ExecutorService getES() {
		return this.es;
	}
	
	public void setDiscoveryStrategy(DeviceDiscoveryStrategy cds) {
		System.out.println("Discovery strategy set");
//		this.cm.setDiscoveryStrategy(cds);
		this.getService().getDeviceManager().setDiscoveryStrategy(cds);
	}

	public IConnection getConnection(String deviceAddress) {
//		return this.getConnector().getConnection(deviceAddress);
		return this.getConnectionUtility().getConnection(deviceAddress);
	}

	protected DummyConnectionUtility getConnectionUtility() {
		return (DummyConnectionUtility) super.getConnectionUtility();
	}
}
