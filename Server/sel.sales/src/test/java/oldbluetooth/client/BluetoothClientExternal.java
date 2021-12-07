package oldbluetooth.client;

import client.controller.IClientController;
import client.external.ClientExternal;
import client.model.IClientModel;
import oldbluetooth.BluetoothDeviceManager;
import oldbluetooth.BluetoothExternalConnector;
import oldbluetooth.BluetoothService;
import oldbluetooth.BluetoothServiceConnectionManager;

public class BluetoothClientExternal extends ClientExternal {

	public BluetoothClientExternal(IClientController controller, IClientModel model, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}

	@Override
	protected BluetoothServiceConnectionManager getServiceConnectionManager() {
		return (BluetoothServiceConnectionManager) super.getServiceConnectionManager();
	}
	
	@Override
	protected BluetoothDeviceManager getDeviceManager() {
		return (BluetoothDeviceManager) super.getDeviceManager();
	}
	
	@Override
	protected BluetoothService getService() {
		return (BluetoothService) super.getService();
	}
	
	@Override
	protected BluetoothDeviceManager initDeviceManager() {
		return new BluetoothDeviceManager(this.getES(), this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return new BluetoothClientService(this.initDeviceManager(), this.getController(), this.getES(), this.getPingPongTimeoutInMillis(),
				this.getMinimalPingPongDelay(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit());
	}

	@Override
	protected BluetoothExternalConnector initConnector() {
		return new BluetoothExternalConnector(this.getService(), this.getController(), this.getES(), this.getPingPongTimeoutInMillis(), this.getMinimalPingPongDelay(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit());
	}

}
