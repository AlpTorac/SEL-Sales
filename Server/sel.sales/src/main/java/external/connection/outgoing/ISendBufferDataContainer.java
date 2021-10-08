package external.connection.outgoing;

import external.message.IMessage;

public interface ISendBufferDataContainer {
	boolean isEmpty();
	boolean add(IMessage message);
	boolean remove(int sequenceNumber);
	void clear();
	int size();
	boolean remove(IMessage message);
	/**
	 * @return The message to be sent without removing it.
	 */
	IMessage getMessageInLine();
}
