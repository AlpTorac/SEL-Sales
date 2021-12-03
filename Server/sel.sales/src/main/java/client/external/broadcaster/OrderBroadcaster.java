package client.external.broadcaster;

import external.broadcaster.Broadcaster;
import external.connection.ConnectionContainer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import model.IModel;
import model.order.OrderData;

public class OrderBroadcaster extends Broadcaster {
	private IModel model;
	private OrderData data;
	
	public OrderBroadcaster(ConnectionContainer cc, IModel model, OrderData data) {
		super(cc);
		this.model = model;
		this.data = data;
	}

	@Override
	public IMessage createMessage() {
		return new Message(MessageContext.ORDER, null, this.model.serialiseOrder(data));
	}
}
