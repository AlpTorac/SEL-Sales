package external.message;

public interface IMessage {
	int getSequenceNumber();
	void setSequenceNumber(int sequenceNumber);
	MessageContext getMessageContext();
	MessageFlag[] getMessageFlags();
	String getSerialisedData();
	IMessage getMinimalAcknowledgementMessage();
	boolean isAcknowledgementMessage();
	boolean hasFlag(MessageFlag f);
	boolean hasContext(MessageContext c);
}