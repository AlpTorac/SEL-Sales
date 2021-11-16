package external.connection;

import java.io.Closeable;

public interface IServiceConnectionManager extends Closeable, IHasConnectionSettings, ConnectionContainer {
//	IConnection getConnection(String deviceAddress);
//	boolean isClosed();
//	void close();
//	void sendMessageTo(String deviceAddress, IMessage message);
//	void broadcastMessage(IMessage message);
//	void receiveKnownDeviceData(IDeviceData[] deviceData);

	boolean isConnectionAllowed(String deviceAddress);
	void makeNewConnectionThread();
}
