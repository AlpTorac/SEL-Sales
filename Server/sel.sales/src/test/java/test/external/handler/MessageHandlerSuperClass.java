package test.external.handler;

import org.junit.jupiter.api.Assertions;

import external.handler.MessageHandler;
import external.message.IMessage;

public abstract class MessageHandlerSuperClass {
	private MessageHandler handler;
	
	public void setHandler(MessageHandler handler) {
		this.handler = handler;
	}
	protected void handleMessageSuccessfulTest(String serialisedMessage) {
		Assertions.assertTrue(handler.handleMessage(serialisedMessage));
	}
	protected void handleMessageFailTest(String serialisedMessage) {
		Assertions.assertFalse(handler.handleMessage(serialisedMessage));
	}
	protected void verificationSuccessfulTest(IMessage message) {
		Assertions.assertTrue(handler.verify(message));
	}
	protected void verificationFailTest(IMessage message) {
		Assertions.assertFalse(handler.verify(message));
	}
	protected void actionSuccessfulTest(IMessage message) {
		Assertions.assertTrue(handler.performNeededAction(message));
	}
	protected void actionFailTest(IMessage message) {
		Assertions.assertFalse(handler.performNeededAction(message));
	}
}
