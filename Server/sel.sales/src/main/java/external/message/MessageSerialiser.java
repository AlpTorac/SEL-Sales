package external.message;

public class MessageSerialiser implements IMessageSerialiser {
	private MessageFormat format;
	
	public MessageSerialiser(MessageFormat format) {
		this.format = format;
	}
	@Override
	public String serialise(IMessage message) {
		String serialisedMessage = this.getMessageStart();
		serialisedMessage += this.serialiseSequenceNumber(message.getSequenceNumber()) + this.getDataFieldSeparatorForString();
		serialisedMessage += this.serialiseContext(message.getMessageContext()) + this.getDataFieldSeparatorForString();
		serialisedMessage += this.serialiseFlags(message.getMessageFlags()) + this.getDataFieldSeparatorForString();
		serialisedMessage += this.serialiseData(message.getSerialisedData()) + this.getMessageEnd();
		return serialisedMessage;
	}
	protected String serialiseData(String serialisedData) {
		return serialisedData;
	}
	protected String serialiseFlags(MessageFlag[] messageFlags) {
		if (messageFlags == null) {
			return "";
		}
		if (messageFlags.length == 0) {
			return "";
		}
		String serialisedFlags = "";
		// take the last element out of the loop to end the flag field
		for (int i = 0; i < messageFlags.length - 1; i++) {
			serialisedFlags += messageFlags[i].toString() + this.getDataFieldElementSeparatorForString();
		}
		serialisedFlags += messageFlags[messageFlags.length - 1].toString();
		return serialisedFlags;
	}
	protected String serialiseContext(MessageContext messageContext) {
		if (messageContext == null) {
			return "";
		}
		return messageContext.toString();
	}
	protected String serialiseSequenceNumber(int sequenceNumber) {
		return String.valueOf(sequenceNumber);
	}
	protected String getDataFieldSeparatorForString() {
		return this.format.getDataFieldSeparatorForString();
	}
	
	protected String getDataFieldElementSeparatorForString() {
		return this.format.getDataFieldElementSeparatorForString();
	}

	protected String getMessageEnd() {
		return this.format.getMessageEnd();
	}

	protected String getMessageStart() {
		return this.format.getMessageStart();
	}
}
