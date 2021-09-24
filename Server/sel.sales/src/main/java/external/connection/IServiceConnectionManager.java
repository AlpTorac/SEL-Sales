package external.connection;

import external.message.IMessage;

public interface IServiceConnectionManager {
	IConnection getConnection(String clientAddress);
	void acceptIncomingConnection();
	boolean isConnectionAllowed(String clientAddress);
	void close();
	void broadcastMessage(IMessage message);
}
