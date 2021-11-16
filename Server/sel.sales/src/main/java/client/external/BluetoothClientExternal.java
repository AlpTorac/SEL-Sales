package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.bluetooth.BluetoothDeviceManager;
import external.bluetooth.BluetoothExternalConnector;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;

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
		return new BluetoothDeviceManager(es, this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return new BluetoothClientService(this.initDeviceManager(), this.getController(), es, this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}

	@Override
	protected BluetoothExternalConnector initConnector() {
		return new BluetoothExternalConnector(this.getService(), this.getController(), es, this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}

}
