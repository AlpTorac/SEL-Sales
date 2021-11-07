package external.connection;

import external.client.IClientManager;
import model.connectivity.IClientData;
import model.settings.ISettings;

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
	void receiveSettings(ISettings settings);
}
