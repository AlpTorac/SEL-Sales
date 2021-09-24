package external.buffer;

import external.message.IMessage;

public interface ISendBufferDataContainer {
	boolean isEmpty();
	boolean add(IMessage message);
	boolean remove(int sequenceNumber);
	default boolean remove(IMessage message) {
		return this.remove(message.getSequenceNumber());
	}
	/**
	 * @return The message to be sent without removing it.
	 */
	IMessage getMessageInLine();
	/**
	 * @return The message in line.
	 */
	IMessage removeMessageInLine();
}
