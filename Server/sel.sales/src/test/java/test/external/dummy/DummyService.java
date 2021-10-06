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
	public void publish() {
		this.scm = new DummyServiceConnectionManager(this.getClientManager(), this.getController(), es);
		this.scm.makeNewConnectionThread();
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
