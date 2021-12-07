package test.external.dummy;

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
		this(controller, model, connUtil,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
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

	@Override
	public void refreshOrders() {
		// TODO Auto-generated method stub
		
	}

}
