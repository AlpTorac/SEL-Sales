package external.message;

public abstract class MessageParser implements IMessageParser {
	private MessageFormat format;
	
	protected MessageParser(MessageFormat format) {
		this.format = format;
	}
	
	@Override
	public Message parseMessage(String message) {
//		String messageBody = this.parseMessageBody(message);
		String messageBody = this.getDataBody(message, this.getMessageStart(), this.getMessageEnd());
		String[] fields = this.parseDataFields(messageBody);
		int sequenceNumber = IMessage.DEFAULT_SEQUENCE_NUMBER;
		if (fields.length > 0 && fields[0] != null && fields[0].length() > 0) {
			sequenceNumber = this.parseSequenceNumber(fields[0]);
		}
		MessageContext context = null;
		if (fields.length > 1 && fields[1] != null && fields[1].length() > 0) {
			context = this.parseContext(fields[1]);
		}
		MessageFlag[] flags = null;
		if (fields.length > 2 && fields[2] != null && fields[2].length() > 0) {
			flags = this.parseFlags(fields[2]);
		}
		String serialisedData = "";
		if (fields.length > 3 && fields[3] != null && fields[3].length() > 0) {
			serialisedData = this.parseSerialisedData(fields[3]);
		}
		return new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	protected String parseMessageBody(String message) {
		int startIndex = 0;
		int endIndex = message.length();
		if (message.startsWith(this.getMessageStart())) {
			int len = this.getMessageStart().length();
			startIndex = len == 0 ? 0 : len - 1;
		}
		if (message.endsWith(this.getMessageEnd())) {
			endIndex -= this.getMessageEnd().length();
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
		if (serialisedFlags == null) {
			return null;
		} else if (serialisedFlags.equals("")) {
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
