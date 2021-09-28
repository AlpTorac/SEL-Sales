package external.connection;

import external.message.IMessage;

public interface IMessageSendingStrategy {
	boolean sendMessage(IConnection os, IMessage message);
}
