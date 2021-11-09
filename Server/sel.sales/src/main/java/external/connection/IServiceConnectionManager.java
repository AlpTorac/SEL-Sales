package external.connection;

import java.io.Closeable;

import external.message.IMessage;
import model.connectivity.IDeviceData;

public interface IServiceConnectionManager extends Closeable, IHasConnectionSettings {
	IConnection getConnection(String deviceAddress);
	boolean isConnectionAllowed(String deviceAddress);
	boolean isClosed();
	void close();
	void sendMessageTo(String deviceAddress, IMessage message);
	void broadcastMessage(IMessage message);
	void makeNewConnectionThread();
	void receiveKnownDeviceData(IDeviceData[] deviceData);
}
