package test.external.dummy;

import java.util.Collection;

import external.device.DeviceDiscoveryStrategy;
import external.device.IDevice;

public class DummyDeviceDiscoveryStrategy extends DeviceDiscoveryStrategy {
	private Collection<IDevice> Devices;

	public void setDiscoveredDevices(Collection<IDevice> Devices) {
		this.Devices = Devices;
	}
	
	@Override
	public Collection<IDevice> discoverDevices() {
		return Devices;
	}
}
