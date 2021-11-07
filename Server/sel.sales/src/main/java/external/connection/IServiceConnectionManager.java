package external.connection;

import java.io.Closeable;

import external.message.IMessage;
import model.connectivity.IClientData;

public interface IServiceConnectionManager extends Closeable, IHasConnectionSettings {
	IConnection getConnection(String clientAddress);
	boolean isConnectionAllowed(String clientAddress);
	boolean isClosed();
	void close();
	void sendMessageTo(String clientAddress, IMessage message);
	void broadcastMessage(IMessage message);
	void makeNewConnectionThread();
	void receiveKnownClientData(IClientData[] clientData);
}
