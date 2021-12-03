package client.external.connection.incoming;

import client.controller.ClientSpecificEvent;
import controller.IController;
import external.handler.MessageHandler;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;
import model.order.OrderData;

public class OrderAcknowledgementHandler extends MessageHandler {
	private IController controller;
	
	public OrderAcknowledgementHandler(IMessageParser parser, IController controller) {
		super(parser);
		this.controller = controller;
	}

	@Override
	public boolean verify(IMessage message) {
		return message.hasContext(MessageContext.ORDER) && message.isAcknowledgementMessage();
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		if (message.getSerialisedData() != null && this.controller.getModel() != null) {
			OrderData orderData = this.controller.getModel().getOrderHelper().deserialiseOrderData(message.getSerialisedData());
			this.controller.handleApplicationEvent(ClientSpecificEvent.ORDER_SENT, new Object[] {orderData.getID().toString()});
			System.out.println("Order acknowledgement received for: ------------------------------------------ " + orderData.getID().toString());
		}
		return true;
	}

}
