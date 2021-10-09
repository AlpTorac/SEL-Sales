package external;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;

public class BluetoothServerService extends BluetoothService {

	public BluetoothServerService(BluetoothClientManager clientManager, IController controller,
			ExecutorService es) {
		super(new UUID(0x1111), "SEL_Service", clientManager, controller, es);
	}
	@Override
	public void publish() {
		this.scm = new BluetoothServiceConnectionManager(this, this.getClientManager(), this.getController(), es, 10000, 2000, 10);
		this.scm.makeNewConnectionThread();
	}
}
