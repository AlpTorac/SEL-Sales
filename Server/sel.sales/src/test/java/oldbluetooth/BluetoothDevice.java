package oldbluetooth;

import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import external.device.IDevice;

public class BluetoothDevice implements IDevice {

	private RemoteDevice device;
	
	BluetoothDevice(RemoteDevice device) {
		this.device = device;
	}

	@Override
	public String getDeviceName() {
		try {
			return this.device.getFriendlyName(false);
		} catch (IOException e) {
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getDeviceAddress() {
		return this.device.getBluetoothAddress();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDevice)) {
			return false;
		}
		return this.getDeviceAddress().equals(((IDevice) o).getDeviceAddress());
	}

	@Override
	public RemoteDevice getDeviceObject() {
		return this.device;
	}
}
