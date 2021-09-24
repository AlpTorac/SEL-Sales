package external.handler;

import external.message.IMessage;
import external.message.IMessageParser;

public interface IMessageHandler {
	default boolean handleMessage(String message) {
		IMessage m = this.verifyMessageFormat(message);
		return this.verify(m) && this.acknowledge(m) && this.performNeededAction(m);
	}
	default IMessage verifyMessageFormat(String message) {
		return this.getMessageParser().parseMessage(message);
	}
	boolean verify(IMessage message);
	boolean acknowledge(IMessage message);
	boolean performNeededAction(IMessage message);
	IMessageParser getMessageParser();
}
