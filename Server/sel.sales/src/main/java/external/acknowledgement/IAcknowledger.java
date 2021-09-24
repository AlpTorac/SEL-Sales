package external.acknowledgement;

import external.message.IMessage;

public interface IAcknowledger {
	boolean acknowledge(IMessage message);
}
