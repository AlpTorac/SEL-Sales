package external.message;

public abstract class MessageParser implements IMessageParser {
	private MessageFormat format;
	
	MessageParser(MessageFormat format) {
		this.format = format;
	}
	
	@Override
	public Message parseMessage(String message) {
		String messageBody = this.parseMessageBody(message);
		String[] fields = this.parseDataFields(messageBody);
		int sequenceNumber = this.parseSequenceNumber(fields[0]);
		MessageContext context = this.parseContext(fields[1]);
		MessageFlag[] flags = this.parseFlags(fields[2]);
		String serialisedData = this.parseSerialisedData(fields[3]);
		return new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	protected String parseMessageBody(String message) {
		int startIndex = 0;
		int endIndex = 0;
		if (message.startsWith(this.getMessageStart())) {
			startIndex = this.getMessageStart().length() - 1;
		}
		if (message.endsWith(this.getMessageEnd())) {
			endIndex = message.length() - this.getMessageEnd().length();
		}
		return message.substring(startIndex, endIndex);
	}
	protected int parseSequenceNumber(String sequenceNumber) {
		return Integer.valueOf(sequenceNumber);
	}
	protected String[] parseDataFields(String messageBody) {
		return messageBody.split(this.getDataFieldSeparator());
	}
	protected String[] parseDataFieldElements(String messageField) {
		return messageField.split(this.getDataFieldElementSeparator());
	}
	protected String parseSerialisedData(String serialisedData) {
		return serialisedData;
	}
	protected MessageFlag[] parseFlags(String serialisedFlags) {
		if (serialisedFlags == null || serialisedFlags.equals("")) {
			return null;
		}
		String[] flags = this.parseDataFieldElements(serialisedFlags);
		MessageFlag[] parsedFlags = new MessageFlag[flags.length];
		for (int i = 0; i < parsedFlags.length; i++) {
			parsedFlags[i] = MessageFlag.stringToMessageFlag(flags[i]);
		}
		return parsedFlags;
	}
	protected MessageContext parseContext(String serialisedContext) {
		return MessageContext.stringToMessageContext(serialisedContext);
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
