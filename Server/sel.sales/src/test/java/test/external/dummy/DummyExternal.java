package test.external.dummy;

import controller.IController;
import external.External;
import external.device.DeviceDiscoveryStrategy;
import model.IModel;
import external.connection.IService;

public class DummyExternal extends External {
//	private IDeviceManager cm;
	private String id;
	private String name;
	
	public DummyExternal(String id, String name, IController controller, IModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
//		this.cm = new DummyDeviceManager(es, controller);
		this.id = id;
		this.name = name;
		this.setService(this.initService());
		this.getService().publish();
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
				new DummyDeviceManager(es, this.getController()), 
				this.getController(), 
				es, 
				this.getPingPongTimeout(), 
				this.getMinimalPingPongDelay(), 
				this.getSendTimeout(), 
				this.getResendLimit());
	}

}
