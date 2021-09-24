package external.acknowledgement;

import external.message.IMessage;

public interface IAcknowledgementStrategy {
	IMessage generateAcknowledgementMessage(IMessage message);
}
