package external.standard;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.IConnectionUtility;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IConnectionObject;
import external.connection.ServiceConnectionManager;
import external.connection.StandardConnectionManager;
import external.connection.incoming.IConnectionNotifier;

public class StandardServiceConnectionManager extends ServiceConnectionManager {
	
	private IConnectionUtility connUtil;
	private IConnectionNotifier connNotifier;
	
//	public BluetoothServiceConnectionManager(BluetoothService service, BluetoothDeviceManager manager, IController controller, ExecutorService es) {
//		super(manager, controller, es);
//		try {
//			this.connNotifier = (StreamConnectionNotifier) Connector.open(service.getURL());
//		} catch (IOException e) {
////			e.printStackTrace();
//		}
//	}
	
	public StandardServiceConnectionManager(StandardService service, StandardDeviceManager manager, IController controller, ExecutorService es,
			IConnectionUtility connUtil, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(manager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.connUtil = connUtil;
		this.connNotifier = this.connUtil.publishService(service);
//		this.setPingPongTimeout(pingPongTimeout);
//		this.setSendTimeout(sendTimeout);
//		this.setResendLimit(resendLimit);
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new StandardConnection((IConnectionObject) connectionObject);
	}

	@Override
	protected Object getConnectionObject() {
		try {
			IConnectionObject connObject = connNotifier.acceptAndOpen();
			if (!this.isConnectionAllowed(connObject.getTargetAddress())) {
				connObject.close();
				connObject = connNotifier.acceptAndOpen();
			}
			System.out.println("Connection established");
			this.makeNewConnectionThread();
			System.out.println("New connection thread created");
			return connObject;
		} catch (IOException e) {
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout, int resendLimit, long minimalPingPongDelay) {
		return new StandardConnectionManager(controller, conn, es, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay);
	}
}
