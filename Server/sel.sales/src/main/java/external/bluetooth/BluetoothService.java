package external.bluetooth;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.connection.Service;

public abstract class BluetoothService extends Service {
	public BluetoothService(UUID id, String name, BluetoothDeviceManager deviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id, name, deviceManager, controller, es, pingPongTimeout,
				minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	@Override
	public UUID getID() {
		return (UUID) super.getID();
	}
	
	@Override
	public abstract void publish();
	
//	@Override
//	public void publish() {
//		this.scm = new BluetoothServiceConnectionManager(this, this.getDeviceManager(), this.getController(), es);
//		this.scm.makeNewConnectionThread();
//	}

	@Override
	public String generateURL() {
		return "btspp://localhost:" + this.getID() + ";name=" + this.getName();
	}
	
	@Override
	public BluetoothDeviceManager getDeviceManager() {
		return (BluetoothDeviceManager) super.getDeviceManager();
	}
}
