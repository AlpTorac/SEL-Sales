package client.external.connection.incoming;

import client.controller.ClientSpecificEvent;
import controller.IController;
import external.handler.MessageHandler;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;

public class MenuHandler extends MessageHandler {
	private IController controller;
	
	public MenuHandler(IMessageParser parser, IController controller) {
		super(parser);
		this.controller = controller;
	}

	@Override
	public boolean verify(IMessage message) {
		return !message.isAcknowledgementMessage() && message.hasContext(MessageContext.MENU);
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		this.controller.handleApplicationEvent(ClientSpecificEvent.MENU_RECEIVED, new Object[] {message.getSerialisedData()});
		return true;
	}

}
