package external.client;

import java.util.Collection;

public abstract class ClientDiscoveryStrategy {
	public abstract Collection<IClient> discoverClients();
}
