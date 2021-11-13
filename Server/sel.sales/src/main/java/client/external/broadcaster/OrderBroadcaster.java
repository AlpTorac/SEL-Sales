package client.external.broadcaster;

import external.broadcaster.Broadcaster;
import external.connection.IServiceConnectionManager;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import model.IModel;
import model.order.IOrderData;

public class OrderBroadcaster extends Broadcaster {
	private IModel model;
	private IOrderData data;
	
	protected OrderBroadcaster(IServiceConnectionManager scm, IModel model, IOrderData data) {
		super(scm);
		this.model = model;
		this.data = data;
	}

	@Override
	public IMessage createMessage() {
		return new Message(MessageContext.ORDER, null, this.model.getOrderHelper().serialiseForApp(data));
	}
}
