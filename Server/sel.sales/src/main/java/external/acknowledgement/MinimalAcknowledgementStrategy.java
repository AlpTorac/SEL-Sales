package external.acknowledgement;

import external.message.IMessage;

public class MinimalAcknowledgementStrategy implements IAcknowledgementStrategy {
	@Override
	public IMessage generateAcknowledgementMessage(IMessage message) {
		return message.getMinimalAcknowledgementMessage();
	}
}
