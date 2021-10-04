package test.external.dummy;

import java.util.concurrent.ExecutorService;

import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class DummyClientManager extends ClientManager {
	public DummyClientManager(ExecutorService es) {
		super(es);
	}

	@Override
	public ClientDiscoveryStrategy initDiscoveryStrategy() {
		return new DummyClientDiscoveryStrategy();
	}
}
