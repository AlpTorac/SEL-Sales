package external.message;

public abstract class MessageFormat {
	private String dataFieldSeparator;
	private String dataFieldElementSeparator;
	private String messageEnd;
	private String messageStart;
	
	MessageFormat(String dataFieldSeparator, String dataFieldElementSeparator, String messageStart, String messageEnd) {
		this.dataFieldSeparator = dataFieldSeparator;
		this.dataFieldElementSeparator = dataFieldElementSeparator;
		this.messageStart = messageStart;
		this.messageEnd = messageEnd;
	}
	
	public String getDataFieldSeparator() {
		return dataFieldSeparator;
	}

	public String getDataFieldElementSeparator() {
		return dataFieldElementSeparator;
	}

	public String getMessageEnd() {
		return messageEnd;
	}

	public String getMessageStart() {
		return messageStart;
	}
}
