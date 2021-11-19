package server.external.broadcaster;

import external.broadcaster.Broadcaster;
import external.connection.ConnectionContainer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import model.IModel;

public class DishMenuBroadcaster extends Broadcaster {
	private IModel model;
	public DishMenuBroadcaster(ConnectionContainer cc, IModel model) {
		super(cc);
		this.model = model;
	}
	@Override
	public IMessage createMessage() {
		return new Message(MessageContext.MENU, null, this.model.getDishMenuHelper().serialiseForExternal(this.model.getMenuData()));
	}
}
