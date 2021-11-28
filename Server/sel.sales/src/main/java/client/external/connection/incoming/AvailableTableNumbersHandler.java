package client.external.connection.incoming;

import client.controller.ClientSpecificEvent;
import controller.IController;
import external.handler.MessageHandler;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;

public class AvailableTableNumbersHandler extends MessageHandler {
	private IController controller;
	
	public AvailableTableNumbersHandler(IMessageParser parser, IController controller) {
		super(parser);
		this.controller = controller;
	}

	@Override
	public boolean verify(IMessage message) {
		return !message.isAcknowledgementMessage() && message.hasContext(MessageContext.AVAILABLE_TABLE_NUMBERS);
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		this.controller.handleApplicationEvent(ClientSpecificEvent.AVAILABLE_TABLE_NUMBERS_RECEIVED, new Object[] {message.getSerialisedData()});
		return true;
	}

}
