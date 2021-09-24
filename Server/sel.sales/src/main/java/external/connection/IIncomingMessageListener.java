package external.connection;

import java.io.InputStream;
import java.util.Collection;

import external.handler.IMessageHandler;
import external.message.IMessageParser;

public interface IIncomingMessageListener {
	Collection<IMessageHandler> initMessageHandlers();
	boolean handleMessage(String message);
	void setMessageReadingStrategy(IMessageReadingStrategy mrs);
	IMessageReadingStrategy getMessageReadingStrategy();
	InputStream getInputStream();
	default void handleCurrentMessage() {
		String message = this.getMessageReadingStrategy().readMessage(this.getInputStream());
		if (message != null && !message.equals("")) {
			this.handleMessage(message);
		}
	}
	IMessageReadingStrategy initMessageReadingStrategy();
	IMessageParser initMessageParser();
}
