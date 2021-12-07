package oldbluetooth;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.DeviceDiscoveryStrategy;
import external.device.DeviceManager;

public class BluetoothDeviceManager extends DeviceManager {

	public BluetoothDeviceManager(ExecutorService es, IController controller) {
		super(es, controller);
	}

	@Override
	public BluetoothDevice getDevice(String deviceAddress) {
		return (BluetoothDevice) super.getDevice(deviceAddress);
	}
	
	@Override
	public DeviceDiscoveryStrategy initDiscoveryStrategy() {
		return new BluetoothDeviceDiscoveryStrategy();
	}
}
