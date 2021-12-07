package test.external.dummy;

import client.controller.IClientController;
import client.external.StandardClientExternal;
import client.model.IClientModel;
import external.IConnectionUtility;
import external.connection.IConnection;
import external.device.DeviceDiscoveryStrategy;

public class DummyStandardClientExternal extends StandardClientExternal implements IDummyExternal {
	protected DummyStandardClientExternal(IClientController controller, IClientModel model, IConnectionUtility connUtil, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, connUtil, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		System.out.println("Service: " + this.getService());
	}

	public DummyStandardClientExternal(IClientController controller, IClientModel model, IConnectionUtility connUtil) {
		this(controller, model, connUtil,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
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
