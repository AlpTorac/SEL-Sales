package test.external.dummy;

import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class DummyClientManager extends ClientManager {
	@Override
	public ClientDiscoveryStrategy initDiscoveryStrategy() {
		return new DummyClientDiscoveryStrategy();
	}
}
