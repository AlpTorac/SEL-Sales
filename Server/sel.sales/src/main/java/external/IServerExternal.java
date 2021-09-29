package external;

public interface IServerExternal extends IExternal {
	void discoverClients();
	void addClient(String clientAddress);
	void removeClient(String clientAddress);
	void allowClient(String clientAddress);
	void blockClient(String clientAddress);
	boolean isAllowedToConnect(String clientAddress);
}
