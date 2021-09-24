package external.broadcaster;

import external.connection.IServiceConnectionManager;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;

public class DishMenuBroadcaster extends Broadcaster {
	private String serialisedData;
	public DishMenuBroadcaster(IServiceConnectionManager scm, String serialisedData) {
		super(scm);
		this.serialisedData = serialisedData;
	}

	@Override
	protected IMessage createMessage() {
		return new Message(MessageContext.MENU, null, serialisedData);
	}
}
