package external.handler;

import controller.BusinessEvent;
import controller.IController;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;

public class OrderHandler extends MessageHandler {
	private IController controller;
	
	public OrderHandler(IMessageParser parser,  IController controller) {
		super(parser);
		this.controller = controller;
	}

	@Override
	public boolean performNeededAction(IMessage message) {
//		System.out.println("order acknowledged");
		this.controller.handleApplicationEvent(BusinessEvent.ADD_ORDER, new Object[] {message.getSerialisedData()});
//		System.out.println("order action performed");
		return true;
	}

	@Override
	public boolean verify(IMessage message) {
		return !message.isAcknowledgementMessage() && message.hasContext(MessageContext.ORDER);
	}
}
