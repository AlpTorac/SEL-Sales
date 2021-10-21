package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class DummyClientManager extends ClientManager {
	public DummyClientManager(ExecutorService es) {
		super(es);
	}
	public DummyClientManager(ExecutorService es, IController controller) {
		super(es, controller);
	}
	@Override
	public DummyClient getClient(String clientAddress) {
		return (DummyClient) super.getClient(clientAddress);
	}
	@Override
	public ClientDiscoveryStrategy initDiscoveryStrategy() {
		return new DummyClientDiscoveryStrategy();
	}
}
