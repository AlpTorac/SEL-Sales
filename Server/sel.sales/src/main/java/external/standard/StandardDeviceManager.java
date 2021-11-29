package external.standard;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.IConnectionUtility;
import external.device.DeviceDiscoveryStrategy;
import external.device.DeviceManager;

public class StandardDeviceManager extends DeviceManager {
	private IConnectionUtility connUtil;
	
	public StandardDeviceManager(ExecutorService es, IController controller, IConnectionUtility connUtil) {
		super(es, controller);
		this.connUtil = connUtil;
	}
	
	@Override
	public StandardDevice getDevice(String deviceAddress) {
		return (StandardDevice) super.getDevice(deviceAddress);
	}
	
	@Override
	public DeviceDiscoveryStrategy initDiscoveryStrategy() {
		return new StandardDeviceDiscoveryStrategy(this.connUtil);
	}
}
