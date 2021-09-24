package external.connection;

import java.io.Closeable;

import external.buffer.ISendBuffer;
import external.message.IMessage;

public interface IConnectionManager extends Closeable {
	IConnection getConnection();
	void init();
	ISendBuffer getSendBuffer();
	IIncomingMessageListener getIncomingMessageListener();
	void sendMessage(IMessage message);
	void close();
}