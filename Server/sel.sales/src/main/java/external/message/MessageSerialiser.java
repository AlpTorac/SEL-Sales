package external.message;

public class MessageSerialiser implements IMessageSerialiser {
	private MessageFormat format;
	
	MessageSerialiser(MessageFormat format) {
		this.format = format;
	}
	@Override
	public String serialise(IMessage message) {
		String serialisedMessage = this.getMessageStart();
		serialisedMessage += this.serialiseSequenceNumber(message.getSequenceNumber()) + this.getDataFieldSeparator();
		serialisedMessage += this.serialiseContext(message.getMessageContext()) + this.getDataFieldSeparator();
		serialisedMessage += this.serialiseFlags(message.getMessageFlags()) + this.getDataFieldSeparator();
		serialisedMessage += this.serialiseData(message.getSerialisedData()) + this.getMessageEnd();
		return serialisedMessage;
	}
	protected String serialiseData(String serialisedData) {
		return serialisedData;
	}
	protected String serialiseFlags(MessageFlag[] messageFlags) {
		String serialisedFlags = "";
		// take the last element out of the loop to end the flag field
		for (int i = 0; i < messageFlags.length - 1; i++) {
			serialisedFlags += messageFlags[i].toString() + this.getDataFieldElementSeparator();
		}
		serialisedFlags += messageFlags[messageFlags.length - 1].toString();
		return serialisedFlags;
	}
	protected String serialiseContext(MessageContext messageContext) {
		return messageContext.toString();
	}
	protected String serialiseSequenceNumber(int sequenceNumber) {
		return String.valueOf(sequenceNumber);
	}
	protected String getDataFieldSeparator() {
		return this.format.getDataFieldSeparator();
	}

	protected String getDataFieldElementSeparator() {
		return this.format.getDataFieldElementSeparator();
	}

	protected String getMessageEnd() {
		return this.format.getMessageEnd();
	}

	protected String getMessageStart() {
		return this.format.getMessageStart();
	}
}
