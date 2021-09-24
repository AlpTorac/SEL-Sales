package external.bluetooth;

import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import external.client.IClient;

public class BluetoothClient implements IClient {

	private RemoteDevice device;
	
	BluetoothClient(RemoteDevice device) {
		this.device = device;
	}

	@Override
	public String getClientName() {
		try {
			return this.device.getFriendlyName(false);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getClientAddress() {
		return this.device.getBluetoothAddress();
	}
}