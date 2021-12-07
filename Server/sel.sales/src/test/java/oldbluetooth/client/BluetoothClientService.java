package oldbluetooth.client;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import oldbluetooth.BluetoothDeviceManager;
import oldbluetooth.BluetoothService;
import oldbluetooth.BluetoothServiceConnectionManager;

public class BluetoothClientService extends BluetoothService {
	
	public BluetoothClientService(BluetoothDeviceManager deviceManager, IController controller,
		ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(new UUID(0x1111), "SEL_Service", deviceManager, controller, es,
				pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	@Override
	public void publish() {
		this.scm = new BluetoothServiceConnectionManager(this, this.getDeviceManager(), this.getController(), es,
				this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
		this.scm.makeNewConnectionThread();
	}
}
