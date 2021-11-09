package external.device;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.GeneralEvent;

public class DeviceDiscoveryListener implements IApplicationEventShooter {

	private IController controller;
	private String deviceName;
	private String deviceAddress;
	
	public DeviceDiscoveryListener(IController controller) {
		this.controller = controller;
	}
	
	public void DeviceDiscovered(String deviceName, String deviceAddress) {
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.deviceName, this.deviceAddress};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return GeneralEvent.DEVICE_DISCOVERED;
	}
	
}
