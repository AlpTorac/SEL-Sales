package external.connection;

import java.io.OutputStream;

import external.message.IMessage;

public interface IMessageSendingStrategy {
	boolean sendMessage(OutputStream os, IMessage message);
}
