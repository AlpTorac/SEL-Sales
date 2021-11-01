package external.client;

import java.util.Collection;

import model.connectivity.IClientData;

public interface IClientManager {
	void discoverClients(Runnable afterAction);
	void addClient(String clientAddress);
	void removeClient(String clientAddress);
	void allowClient(String clientAddress);
	void blockClient(String clientAddress);
	boolean isAllowedToConnect(String clientAddress);
	Collection<IClient> getDiscoveredClients();
	void receiveKnownClientData(IClientData[] clientData);
	IClient getClient(String clientAddress);
	ClientDiscoveryStrategy initDiscoveryStrategy();
	void setDiscoveryStrategy(ClientDiscoveryStrategy cds);
	void setClientDiscoveryListener(ClientDiscoveryListener cdl);
	int getClientCount();
}
