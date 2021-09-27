package external.handler;

import external.message.IMessage;
import external.message.IMessageParser;

public abstract class MessageHandler implements IMessageHandler {
	private IMessageParser parser;

	MessageHandler(IMessageParser parser) {
		this.parser = parser;
	}
	
	@Override
	public boolean handleMessage(String message) {
		IMessage m = this.verifyMessageFormat(message);
		return this.verify(m) && this.acknowledge(m) && this.performNeededAction(m);
	}
	
	@Override
	public IMessage verifyMessageFormat(String message) {
		return this.getMessageParser().parseMessage(message);
	}
	
	protected IMessageParser getMessageParser() {
		return this.parser;
	}
	
	public abstract boolean verify(IMessage message);
	public abstract boolean acknowledge(IMessage message);
	public abstract boolean performNeededAction(IMessage message);
}
