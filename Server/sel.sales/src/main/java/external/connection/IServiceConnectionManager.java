package external.connection;

import external.message.IMessage;
import model.connectivity.IClientData;

public interface IServiceConnectionManager {
	IConnection getConnection(String clientAddress);
	boolean isConnectionAllowed(String clientAddress);
	void close();
	void sendMessageTo(String clientAddress, IMessage message);
	void broadcastMessage(IMessage message);
	void makeNewConnectionThread();
	void receiveKnownClientData(IClientData[] clientData);
}
