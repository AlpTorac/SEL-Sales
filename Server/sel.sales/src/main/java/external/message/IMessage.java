package external.message;

public interface IMessage extends Cloneable {
	public static final int DEFAULT_SEQUENCE_NUMBER = 0;
	
	int getSequenceNumber();
	void setSequenceNumber(int sequenceNumber);
	MessageContext getMessageContext();
	MessageFlag[] getMessageFlags();
	String getSerialisedData();
	IMessage getMinimalAcknowledgementMessage();
	boolean isAcknowledgementMessage();
	boolean isPingPongMessage();
	boolean hasFlag(MessageFlag f);
	boolean hasContext(MessageContext c);
	boolean equals(Object o);
	IMessage clone();
}