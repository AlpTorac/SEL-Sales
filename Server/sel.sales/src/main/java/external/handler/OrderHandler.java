package external.handler;

import controller.IController;
import external.acknowledgement.IAcknowledger;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;

public class OrderHandler extends MessageHandler {
	private IAcknowledger acknowledger;
	private IController controller;
	
	public OrderHandler(IMessageParser parser, IAcknowledger acknowledger, IController controller) {
		super(parser);
		this.acknowledger = acknowledger;
		this.controller = controller;
	}
	
	@Override
	public boolean acknowledge(IMessage message) {
		return this.acknowledger.acknowledge(message);
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		this.controller.addOrder(message.getSerialisedData());
		return true;
	}

	@Override
	public boolean verify(IMessage message) {
		return message.hasContext(MessageContext.ORDER);
	}
}
