package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.DeviceDiscoveryStrategy;
import external.device.DeviceManager;

public class DummyDeviceManager extends DeviceManager {
	public DummyDeviceManager(ExecutorService es) {
		super(es);
	}
	public DummyDeviceManager(ExecutorService es, IController controller) {
		super(es, controller);
	}
	@Override
	public DummyDevice getDevice(String DeviceAddress) {
		return (DummyDevice) super.getDevice(DeviceAddress);
	}
	@Override
	public DeviceDiscoveryStrategy initDiscoveryStrategy() {
		return new DummyDeviceDiscoveryStrategy();
	}
}
