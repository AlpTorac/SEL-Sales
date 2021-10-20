package external.bluetooth;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.client.ClientDiscoveryListener;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public abstract class BluetoothService extends Service {
	public BluetoothService(UUID id, String name, BluetoothClientManager clientManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id.toString(), name, clientManager, controller, es, pingPongTimeout,
				minimalPingPongDelay, sendTimeout, resendLimit);
	}

	@Override
	public abstract void publish();
	
//	@Override
//	public void publish() {
//		this.scm = new BluetoothServiceConnectionManager(this, this.getClientManager(), this.getController(), es);
//		this.scm.makeNewConnectionThread();
//	}

	@Override
	public String generateURL() {
		return "btspp://localhost:" + this.getID() + ";name=" + this.getName();
	}
	
	@Override
	public BluetoothClientManager getClientManager() {
		return (BluetoothClientManager) super.getClientManager();
	}
}
