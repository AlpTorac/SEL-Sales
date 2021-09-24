package external.message;

public enum MessageFlag {
	ACKNOWLEDGEMENT("ack");
	
	private final String message;
	
	private MessageFlag(String message) {
		this.message = message;
	}
	
	public static MessageFlag stringToMessageFlag(String message) {
		for (MessageFlag t : values()) {
			if (t.toString().equals(message)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No message type " + message);
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
