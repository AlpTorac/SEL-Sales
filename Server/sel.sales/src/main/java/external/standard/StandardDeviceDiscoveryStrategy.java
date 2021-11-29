package external.standard;

import java.util.Collection;

import external.IConnectionUtility;
import external.device.DeviceDiscoveryStrategy;
import external.device.IDevice;

public class StandardDeviceDiscoveryStrategy extends DeviceDiscoveryStrategy {
	private IConnectionUtility connUtil;
	
	public StandardDeviceDiscoveryStrategy(IConnectionUtility connUtil) {
		this.connUtil = connUtil;
	}
	
	@Override
	public Collection<IDevice> discoverDevices() {
		return this.connUtil.discoverDevices();
	}

}
