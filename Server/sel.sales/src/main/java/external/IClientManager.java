package external;

import java.util.Collection;

public interface IClientManager {
	boolean isAllowedToConnect(String deviceID);
	Collection<IClient> discoverClients();
	ClientDiscoveryStrategy initDiscoveryStrategy();
	void setDiscoveryStrategy(ClientDiscoveryStrategy cds);
	void addClient(IClient client);
	void removeClient(String deviceID);
	void allowClient(String deviceID);
	void blockClient(String deviceID);
}
