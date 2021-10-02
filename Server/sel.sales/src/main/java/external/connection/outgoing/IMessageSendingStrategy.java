package external.connection.outgoing;

import external.connection.IConnection;
import external.message.IMessage;

public interface IMessageSendingStrategy {
	boolean sendMessage(IConnection os, IMessage message);
}
