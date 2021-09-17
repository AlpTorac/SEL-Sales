package external;

import java.util.Collection;

public abstract class ClientDiscoveryStrategy {
	public ClientDiscoveryStrategy() {
		
	}
	public abstract Collection<IClient> discoverClients();
}
