package external.standard;

import external.device.IDevice;

public class StandardDevice implements IDevice {

	private String name;
	private String address;
	private Object deviceObject;
	
	public StandardDevice(String name, String address, Object deviceObject) {
		this.name = name;
		this.address = address;
		this.deviceObject = deviceObject;
	}
	
	@Override
	public String getDeviceName() {
		return this.name;
	}

	@Override
	public String getDeviceAddress() {
		return this.address;
	}

	@Override
	public Object getDeviceObject() {
		return this.deviceObject;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDevice)) {
			return false;
		}
		return this.getDeviceAddress().equals(((IDevice) o).getDeviceAddress());
	}

}
