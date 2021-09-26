package test.external.dummy;

import java.util.Collection;

import external.client.ClientDiscoveryStrategy;
import external.client.IClient;

public class DummyClientDiscoveryStrategy extends ClientDiscoveryStrategy {
	private Collection<IClient> clients;

	public void setDiscoveredClients(Collection<IClient> clients) {
		this.clients = clients;
	}
	
	@Override
	public Collection<IClient> discoverClients() {
		return clients;
	}
}
