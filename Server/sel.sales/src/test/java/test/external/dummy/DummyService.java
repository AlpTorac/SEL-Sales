package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.IClientManager;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public class DummyService extends Service {

	public DummyService(String id, String name, IClientManager clientManager, IController controller, ExecutorService es) {
		super(id, name, clientManager, controller, es);
	}

	@Override
	public IServiceConnectionManager publish() {
		return new DummyServiceConnectionManager(this.getClientManager(), this.getController(), es);
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
