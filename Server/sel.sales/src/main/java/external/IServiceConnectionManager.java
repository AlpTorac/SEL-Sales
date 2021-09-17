package external;

import java.util.Collection;

public interface IServiceConnectionManager {
	IConnection getConnection(String clientAddress);
	Collection<IConnection> getAllOngoingConnections();
	void addAllowedConnection(IConnection connection);
	IConnection acceptIncomingConnection();
	boolean isConnectionAllowed(IConnection connection);
	void close();
	void setClientManager(IClientManager clientManager);
}
