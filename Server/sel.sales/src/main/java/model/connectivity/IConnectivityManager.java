package model.connectivity;

public interface IConnectivityManager {
	void addDiscoveredClient(String clientName, String clientAddress);
	void addKnownClient(String clientAddress);
	void removeKnownClient(String clientAddress);
	void allowKnownClient(String clientAddress);
	void blockKnownClient(String clientAddress);
	void requestClientRediscovery();
	boolean isClientRediscoveryRequested();
	boolean isAllowedToConnect(String clientAddress);
	boolean isConnected(String clientAddress);
	void clientConnected(String clientAddress);
	void clientDisconnected(String clientAddress);
	
	int getKnownClientCount();
	
	IClientData[] getAllKnownClientData();
	IClientData[] getAllDiscoveredClientData();
}
