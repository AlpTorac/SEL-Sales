package external.client;

import java.util.Collection;

public interface IClientManager {
	boolean isAllowedToConnect(String clientAddress);
	Collection<IClient> discoverClients();
	ClientDiscoveryStrategy initDiscoveryStrategy();
	void setDiscoveryStrategy(ClientDiscoveryStrategy cds);
	void addClient(IClient client);
	IClient getClient(String clientAddress);
	void removeClient(String clientAddress);
	void allowClient(String clientAddress);
	void blockClient(String clientAddress);
}
