package external;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;

public class BluetoothServerService extends BluetoothService {

	public BluetoothServerService(BluetoothClientManager clientManager, IController controller,
			ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(new UUID(0x1111), "SEL_Service", clientManager, controller, es,
				pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	@Override
	public void publish() {
		this.scm = new BluetoothServiceConnectionManager(this, this.getClientManager(), this.getController(), es,
				this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
		this.scm.makeNewConnectionThread();
	}
}
