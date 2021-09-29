package external.client;

import java.util.Collection;

public interface IClientManager {
	void discoverClients();
	void addClient(String clientAddress);
	void removeClient(String clientAddress);
	void allowClient(String clientAddress);
	void blockClient(String clientAddress);
	boolean isAllowedToConnect(String clientAddress);
	Collection<IClient> getDiscoveredClients();
	IClient getClient(String clientAddress);
	ClientDiscoveryStrategy initDiscoveryStrategy();
	void setDiscoveryStrategy(ClientDiscoveryStrategy cds);
	int getClientCount();
}
