package test.external.dummy;

import controller.IController;
import external.client.IClientManager;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.connection.ServiceConnectionManager;

public class DummyServiceConnectionManager extends ServiceConnectionManager {

	private int connectionCount;
	
	protected DummyServiceConnectionManager(IService service, IClientManager manager, IController controller) {
		super(service, manager, controller);
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new DummyConnection("client" + connectionCount + "address");
	}

	@Override
	protected Object getConnectionObject() {
		return null;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn) {
		return new DummyConnectionManager(controller, conn, pool);
	}

}
