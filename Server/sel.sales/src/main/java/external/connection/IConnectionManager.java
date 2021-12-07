package external.connection;

import java.io.Closeable;

import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;

public interface IConnectionManager extends Closeable, IHasConnectionSettings {
	IConnection getConnection();
	ISendBuffer getSendBuffer();
	IPingPong getPingPong();
	IMessageReceptionist getIncomingMessageListener();
	void sendMessage(IMessage message);
	void checkCycle();
	void close();
	void setDisconnectionListener(DisconnectionListener dl);
	boolean isClosed();
}