package external.connection;

import java.util.Collection;

import external.handler.IMessageHandler;
import external.message.IMessageParser;

public interface IIncomingMessageListener {
	Collection<IMessageHandler> initMessageHandlers();
	boolean handleMessage(String message);
	boolean handleCurrentMessage();
	IMessageReadingStrategy initMessageReadingStrategy();
	IMessageParser initMessageParser();
}
