package external.connection;

import external.message.IMessage;
import model.connectivity.IDeviceData;

public interface ConnectionContainer {
	IConnection getConnection(String deviceAddress);
	boolean isClosed();
	void close();
	void sendMessageTo(String deviceAddress, IMessage message);
	void broadcastMessage(IMessage message);
	void receiveKnownDeviceData(IDeviceData[] deviceData);
}
