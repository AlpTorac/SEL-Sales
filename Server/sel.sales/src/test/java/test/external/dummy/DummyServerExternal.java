package test.external.dummy;

import external.device.DeviceDiscoveryStrategy;
import external.device.IDeviceManager;
import server.controller.IServerController;
import server.external.ServerExternal;
import server.model.IServerModel;

import external.connection.IConnection;
import external.connection.IService;

public class DummyServerExternal extends ServerExternal implements IDummyExternal {
//	private IDeviceManager cm;
	private String id;
	private String name;
	
	private boolean attemptToReconnect = false;
	
	public DummyServerExternal(String id, String name, IServerController controller, IServerModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
//		this.cm = new DummyDeviceManager(es, controller);
		this.id = id;
		this.name = name;
		System.out.println("Service: " + this.getService());
	}
	
	public DummyServerExternal(String id, String name, IServerController controller, IServerModel model) {
		this(id, name, controller, model,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
	}
	
	public DummyServerExternal(String id, String name, IServerController controller, IServerModel model, boolean attemptToReconnect) {
		this(id, name, controller, model,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
		this.attemptToReconnect = attemptToReconnect;
		this.setService(this.initService());
//		this.getService().publish();
	}
	
	public void setDiscoveryStrategy(DeviceDiscoveryStrategy cds) {
		System.out.println("Discovery strategy set");
//		this.cm.setDiscoveryStrategy(cds);
		this.getService().getDeviceManager().setDiscoveryStrategy(cds);
	}
	
//	@Override
//	public void rediscoverDevices() {
//		System.out.println("Rediscovering");
//		this.cm.discoverDevices();
//	}
//	@Override
//	public void refreshKnownDevices() {
//		System.out.println("Refreshing known Devices");
//		if (this.cm != null) {
//			this.cm.receiveKnownDeviceData(this.getModel().getAllKnownDeviceData());
//		}
//	}
	@Override
	protected IService initService() {
		return new DummyService(
				id, 
				name, 
				this.initDeviceManager(), 
				this.getController(), 
				this.getES(), 
				this.getPingPongTimeoutInMillis(), 
				this.getMinimalPingPongDelay(), 
				this.getSendTimeoutInMillis(), 
				this.getPingPongResendLimit(),
				this.attemptToReconnect);
	}

	@Override
	protected IDeviceManager initDeviceManager() {
		return new DummyDeviceManager(this.getES(), this.getController());
	}

	public IConnection getConnection(String deviceAddress) {
		return this.getService().getServiceConnectionManager().getConnection(deviceAddress);
	}

	@Override
	public void refreshOrders() {
		// TODO Auto-generated method stub
		
	}
}
