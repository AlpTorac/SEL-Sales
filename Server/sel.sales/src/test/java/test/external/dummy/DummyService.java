package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.IClientManager;
import external.connection.IServiceConnectionManager;
import external.connection.Service;

public class DummyService extends Service {

	public DummyService(String id, String name, IClientManager clientManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id, name, clientManager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}

	@Override
	public void publish() {
		this.scm = new DummyServiceConnectionManager(
				this.getClientManager(),
				this.getController(),
				es,
				this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(),
				this.getSendTimeout(),
				this.getResendLimit());
		this.scm.makeNewConnectionThread();
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
