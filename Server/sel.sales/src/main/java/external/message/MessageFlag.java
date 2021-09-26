package external.message;

public enum MessageFlag {
	ACKNOWLEDGEMENT("ack");
	
	private final String message;
	
	private MessageFlag(String message) {
		this.message = message;
	}
	
	public static MessageFlag stringToMessageFlag(String serialisedFlag) {
		for (MessageFlag t : values()) {
			if (t.toString().equals(serialisedFlag)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No message type " + serialisedFlag);
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
