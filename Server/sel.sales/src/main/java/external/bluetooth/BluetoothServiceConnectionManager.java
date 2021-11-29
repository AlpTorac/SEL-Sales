package external.bluetooth;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import javax.bluetooth.RemoteDevice;
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
	
//	public BluetoothServiceConnectionManager(BluetoothService service, BluetoothDeviceManager manager, IController controller, ExecutorService es) {
//		super(manager, controller, es);
//		try {
//			this.connNotifier = (StreamConnectionNotifier) Connector.open(service.getURL());
//		} catch (IOException e) {
////			e.printStackTrace();
//		}
//	}
	
	public BluetoothServiceConnectionManager(BluetoothService service, BluetoothDeviceManager manager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(manager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		try {
			this.connNotifier = (StreamConnectionNotifier) Connector.open(service.getURL());
		} catch (IOException e) {
//			e.printStackTrace();
		}
//		this.setPingPongTimeout(pingPongTimeout);
//		this.setSendTimeout(sendTimeout);
//		this.setResendLimit(resendLimit);
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new BluetoothConnection((StreamConnection) connectionObject);
	}

	@Override
	protected Object getConnectionObject() {
		try {
			StreamConnection connObject = connNotifier.acceptAndOpen();
			if (!this.isConnectionAllowed(RemoteDevice.getRemoteDevice(connObject).getBluetoothAddress())) {
				connObject.close();
				connObject = connNotifier.acceptAndOpen();
			}
			System.out.println("Connection established");
			this.makeNewConnectionThread();
			System.out.println("New connection thread created");
			return connObject;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay) {
		return new StandardConnectionManager(controller, conn, es, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay);
	}
}
