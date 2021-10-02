package external.bluetooth;

import javax.bluetooth.UUID;

import controller.IController;
import external.client.ClientDiscoveryListener;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public class BluetoothService extends Service {
	public BluetoothService(UUID id, String name, BluetoothClientManager clientManager, IController controller) {
		super(id.toString(), name, clientManager, controller);
		this.getClientManager().setClientDiscoveryListener(new ClientDiscoveryListener(this.getController()));
	}

	@Override
	public IServiceConnectionManager publish() {
		return new BluetoothServiceConnectionManager(this, this.getClientManager(), this.getController());
	}

	@Override
	public String generateURL() {
		return "btspp://localhost:" + this.getID() + ";name=" + this.getName();
	}
	
	@Override
	public BluetoothClientManager getClientManager() {
		return (BluetoothClientManager) super.getClientManager();
	}
}
