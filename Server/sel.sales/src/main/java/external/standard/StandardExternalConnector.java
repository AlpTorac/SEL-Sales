package external.standard;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.IConnectionUtility;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.StandardConnectionManager;
import external.connection.outgoing.ExternalConnector;
import external.device.IDevice;

public class StandardExternalConnector extends ExternalConnector {

	private IConnectionUtility connUtil;
	
	public StandardExternalConnector(IService service, IController controller, ExecutorService es, IConnectionUtility connUtil, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(service, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.connUtil = connUtil;
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new StandardConnection((IConnectionObject) connectionObject);
	}

	@Override
	protected Object getConnectionObject(Object serviceID, IDevice serviceHost) {
		String address = this.getConnectionAddress(serviceID, serviceHost);
		System.out.println("Connecting to service: " + address);
		IConnectionObject conn = this.connUtil.openConnection(address);
		System.out.println("Connected to service");
		return conn;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout,
			int resendLimit, long minimalPingPongDelay) {
		return new StandardConnectionManager(controller, conn, es, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay);
	}

	@Override
	protected String getConnectionAddress(Object serviceID, IDevice serviceHost) {
		return this.connUtil.getServiceConnectionURL(serviceID, serviceHost);
	}
}
