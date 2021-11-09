package test.external.dummy;

import external.device.IDevice;

public class DummyDevice implements IDevice {
	private String DeviceName;
	private String DeviceAddress;
	
	public DummyDevice(String DeviceName, String DeviceAddress) {
		this.DeviceName = DeviceName;
		this.DeviceAddress = DeviceAddress;
	}
	
	@Override
	public String getDeviceName() {
		return this.DeviceName;
	}

	@Override
	public String getDeviceAddress() {
		return this.DeviceAddress;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof IDevice)) {
			return false;
		}
		return this.getDeviceAddress().equals(((IDevice) o).getDeviceAddress());
	}
}
