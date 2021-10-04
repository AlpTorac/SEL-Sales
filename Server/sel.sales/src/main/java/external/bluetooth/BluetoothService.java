package external.bluetooth;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.client.ClientDiscoveryListener;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public class BluetoothService extends Service {
	public BluetoothService(UUID id, String name, BluetoothClientManager clientManager, IController controller, ExecutorService es) {
		super(id.toString(), name, clientManager, controller, es);
		this.getClientManager().setClientDiscoveryListener(new ClientDiscoveryListener(this.getController()));
	}

	@Override
	public IServiceConnectionManager publish() {
		IServiceConnectionManager scm = new BluetoothServiceConnectionManager(this, this.getClientManager(), this.getController(), es);
		scm.makeNewConnectionThread();
		return scm;
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
