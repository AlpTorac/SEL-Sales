package server.external;

import external.bluetooth.BluetoothDeviceManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;
import server.controller.IServerController;
import server.model.IServerModel;

public class BluetoothServerExternal extends ServerExternal {

	public BluetoothServerExternal(IServerController controller, IServerModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
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
		return new BluetoothServerService(this.initDeviceManager(), this.getController(), es, this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}
}
