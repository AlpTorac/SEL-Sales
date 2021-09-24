package external.connection;

import external.client.IClientManager;

public interface IService {
	String getID();
	String getName();
	String getURL();
	IServiceConnectionManager publish();
	IServiceConnectionManager getServiceConnectionManager();
	IClientManager getClientManager();
	String generateURL();
}
