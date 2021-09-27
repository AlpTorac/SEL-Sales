package external.handler;

import external.message.IMessage;

public interface IMessageHandler {
	boolean handleMessage(String message);
	IMessage verifyMessageFormat(String message);
}
