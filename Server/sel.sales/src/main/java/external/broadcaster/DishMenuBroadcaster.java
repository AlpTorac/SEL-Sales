package external.broadcaster;

import external.connection.IServiceConnectionManager;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import model.IModel;

public class DishMenuBroadcaster extends Broadcaster {
	private IModel model;
	public DishMenuBroadcaster(IServiceConnectionManager scm, IModel model) {
		super(scm);
		this.model = model;
	}

	@Override
	public IMessage createMessage() {
		return new Message(MessageContext.MENU, null, this.model.getDishMenuHelper().serialiseForExternal(this.model.getMenuData()));
	}
}
