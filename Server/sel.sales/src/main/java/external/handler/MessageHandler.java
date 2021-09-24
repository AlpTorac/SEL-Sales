package external.handler;

import external.message.IMessageParser;

public abstract class MessageHandler implements IMessageHandler {
	protected IMessageParser parser;

	MessageHandler(IMessageParser parser) {
		this.parser = parser;
	}
	
	@Override
	public IMessageParser getMessageParser() {
		return this.parser;
	}
}
