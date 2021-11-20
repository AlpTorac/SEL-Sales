package test.external.dummy;

import java.util.ArrayList;
import java.util.Collection;

import external.device.DeviceDiscoveryStrategy;
import external.device.IDevice;

public class DummyDeviceDiscoveryStrategy extends DeviceDiscoveryStrategy {
	private Collection<IDevice> Devices = new ArrayList<IDevice>();
	
	public void addDiscoveredDevices(Collection<IDevice> Devices) {
		this.Devices.addAll(Devices);
	}

	public void addDiscoveredDevices(IDevice... devices) {
		for (IDevice d : devices) {
			this.Devices.add(d);
		}
	}
	
	public void setDiscoveredDevices(Collection<IDevice> Devices) {
		this.Devices = Devices;
	}
	
	@Override
	public Collection<IDevice> discoverDevices() {
		return Devices;
	}
}
