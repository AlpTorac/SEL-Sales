package external.bluetooth;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import controller.IController;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.ServiceConnectionManager;
import external.connection.StandardConnectionManager;

public class BluetoothServiceConnectionManager extends ServiceConnectionManager {
	
	private StreamConnectionNotifier connNotifier;
	
	public BluetoothServiceConnectionManager(BluetoothService service, BluetoothClientManager manager, IController controller) {
		super(manager, controller);
		try {
			this.connNotifier = (StreamConnectionNotifier) Connector.open(service.getURL());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new BluetoothConnection((StreamConnection) connectionObject);
	}

	@Override
	protected Object getConnectionObject() {
		try {
			return connNotifier.acceptAndOpen();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn) {
		return new StandardConnectionManager(controller, conn, pool);
	}
}
