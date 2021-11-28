package server.external.broadcaster;

import external.broadcaster.Broadcaster;
import external.connection.ConnectionContainer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import model.IModel;
import model.settings.SettingsField;

public class TableNumberBroadcaster extends Broadcaster {
	private IModel model;
	
	public TableNumberBroadcaster(ConnectionContainer cc, IModel model) {
		super(cc);
		this.model = model;
	}
	
	@Override
	public IMessage createMessage() {
		return new Message(MessageContext.AVAILABLE_TABLE_NUMBERS, null, this.model.getSettings().getSetting(SettingsField.TABLE_NUMBERS));
	}

}
