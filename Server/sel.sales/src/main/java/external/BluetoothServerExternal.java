package external;

import javax.bluetooth.UUID;

import controller.IController;
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;
import model.IModel;

public class BluetoothServerExternal extends ServerExternal {

	public BluetoothServerExternal(IController controller, IModel model) {
		super(controller, model);
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
		return new BluetoothClientManager(this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return this.initBluetoothService(new UUID(0x1111), "SEL_Service");
	}
	
	private BluetoothService initBluetoothService(UUID id, String name) {
		return new BluetoothService(id, name, this.initClientManager(), this.getController());
	}
}
