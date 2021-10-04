package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.IClientManager;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.ServiceConnectionManager;

public class DummyServiceConnectionManager extends ServiceConnectionManager {

	private volatile DummyClient currentClient;
	
	public DummyServiceConnectionManager(IClientManager manager, IController controller, ExecutorService es) {
		super(manager, controller, es);
	}

	public void setCurrentConnectionObject(DummyClient currentClient) {
		this.currentClient = currentClient;
	}
	
//	@Override
//	protected boolean addConnection(IConnection conn) {
//		boolean result = super.addConnection(conn);
//		System.out.println(conn.getTargetClientAddress() + " added");
//		return result;
//	}
	
	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new DummyConnection(((DummyClient) getConnectionObject()).getClientAddress());
	}

	@Override
	protected Object getConnectionObject() {
		return currentClient;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn) {
		return new DummyConnectionManager(controller, conn, es);
	}

}
