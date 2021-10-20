package external.connection;

import external.client.IClientManager;
import model.connectivity.IClientData;

public interface IService {
	String getID();
	String getName();
	String getURL();
	void publish();
	IServiceConnectionManager getServiceConnectionManager();
	IClientManager getClientManager();
	String generateURL();
	void receiveKnownClientData(IClientData[] clientData);
	long getMinimalPingPongDelay();
	int getResendLimit();
	long getPingPongTimeout();
	long getSendTimeout();
}
