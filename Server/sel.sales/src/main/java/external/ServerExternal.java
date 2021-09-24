package external;

import javax.bluetooth.UUID;

import controller.IController;
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;
import external.broadcaster.DishMenuBroadcaster;
import external.broadcaster.IBroadcaster;
import model.IModel;
import model.dish.IDishMenuData;

public class ServerExternal extends External {
	
	public ServerExternal(IController controller, IModel model) {
		super(controller, model);
	}
	
	@Override
	protected BluetoothClientManager getClientManager() {
		return (BluetoothClientManager) super.getClientManager();
	}
	
	@Override
	protected BluetoothService initService() {
		return this.initBluetoothService(new UUID("0x1111", true), "SEL_Service");
	}
	
	private BluetoothService initBluetoothService(UUID id, String name) {
		return new BluetoothService(id, name, this.getClientManager(), this.getController());
	}
	
	@Override
	public void sendMenuData() {
		IDishMenuData data = this.getModel().getMenuData();
		IBroadcaster broadcaster = new DishMenuBroadcaster(this.getServiceConnectionManager(),
				this.getModel().getExternalDishMenuSerialiser().serialise(data));
		broadcaster.broadcast();
	}
	
	@Override
	protected BluetoothService getService() {
		return (BluetoothService) super.getService();
	}
	@Override
	protected BluetoothServiceConnectionManager getServiceConnectionManager() {
		return (BluetoothServiceConnectionManager) super.getServiceConnectionManager();
	}
	
	@Override
	protected BluetoothClientManager initClientManager() {
		return new BluetoothClientManager();
	}
}
