package external.broadcaster;

import external.message.IMessage;

public interface IBroadcaster {
	void broadcast();
	IMessage createMessage();
}
