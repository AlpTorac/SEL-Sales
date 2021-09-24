package external.message;

public enum MessageContext {
	ORDER("order"),
	MENU("menu");
	
	private final String message;
	
	private MessageContext(String message) {
		this.message = message;
	}
	
	public static MessageContext stringToMessageContext(String message) {
		for (MessageContext t : values()) {
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
