package external.handler;

import external.message.IMessage;
import external.message.IMessageParser;
import model.exceptions.NoSuchMessageContextException;
import model.exceptions.NoSuchMessageFlagException;

public abstract class MessageHandler implements IMessageHandler {
	private IMessageParser parser;

	MessageHandler(IMessageParser parser) {
		this.parser = parser;
	}
	
	@Override
	public boolean handleMessage(String message) {
		IMessage m = null;
		try {
			m = this.verifyMessageFormat(message);
		} catch (NumberFormatException | NoSuchMessageContextException | NoSuchMessageFlagException e) {
			return false;
		}
		return this.verify(m) && this.performNeededAction(m);
	}
	
	@Override
	public IMessage verifyMessageFormat(String message) {
		return this.getMessageParser().parseMessage(message);
	}
	
	protected IMessageParser getMessageParser() {
		return this.parser;
	}
	
	public abstract boolean verify(IMessage message);
	public abstract boolean performNeededAction(IMessage message);
}
