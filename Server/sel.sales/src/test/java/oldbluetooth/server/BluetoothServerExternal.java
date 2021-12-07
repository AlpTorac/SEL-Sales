package oldbluetooth.server;

import oldbluetooth.BluetoothDeviceManager;
import oldbluetooth.BluetoothService;
import oldbluetooth.BluetoothServiceConnectionManager;
import server.controller.IServerController;
import server.external.ServerExternal;
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
		return new BluetoothDeviceManager(this.getES(), this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return new BluetoothServerService(this.initDeviceManager(), this.getController(), this.getES(), this.getPingPongTimeoutInMillis(),
				this.getMinimalPingPongDelay(), this.getSendTimeoutInMillis(), this.getPingPongResendLimit());
	}
}
