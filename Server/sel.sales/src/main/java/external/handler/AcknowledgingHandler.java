package external.handler;

import external.acknowledgement.IAcknowledger;
import external.message.IMessage;
import external.message.IMessageParser;

public class AcknowledgingHandler extends MessageHandler {

	private IAcknowledger acknowledger;
	
	public AcknowledgingHandler(IMessageParser parser, IAcknowledger acknowledger) {
		super(parser);
		this.acknowledger = acknowledger;
	}

	@Override
	public boolean verify(IMessage message) {
		return !message.isPingPongMessage();
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		return this.acknowledger.acknowledge(message);
	}

}
