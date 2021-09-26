package test.external.dummy;

import controller.IController;
import external.client.IClientManager;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public class DummyService extends Service {

	public DummyService(String id, String name, IClientManager clientManager, IController controller) {
		super(id, name, clientManager, controller);
	}

	@Override
	public IServiceConnectionManager publish() {
		return new DummyServiceConnectionManager(this, this.getClientManager(), this.getController());
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
