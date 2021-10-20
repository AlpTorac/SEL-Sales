package external;

import controller.IController;
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;
import model.IModel;

public class BluetoothServerExternal extends ServerExternal {

	public BluetoothServerExternal(IController controller, IModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	@Override
	protected BluetoothServiceConnectionManager getServiceConnectionManager() {
		return (BluetoothServiceConnectionManager) super.getServiceConnectionManager();
	}
	
	@Override
	protected BluetoothClientManager getClientManager() {
		return (BluetoothClientManager) super.getClientManager();
	}
	
	@Override
	protected BluetoothService getService() {
		return (BluetoothService) super.getService();
	}
	
	@Override
	protected BluetoothClientManager initClientManager() {
		return new BluetoothClientManager(es, this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return new BluetoothServerService(this.initClientManager(), this.getController(), es, this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}
}
