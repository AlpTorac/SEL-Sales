package external.device;

import java.util.Collection;

public abstract class DeviceDiscoveryStrategy {
	
	protected DeviceDiscoveryStrategy() {
		
	}
	
	public abstract Collection<IDevice> discoverDevices();
}
