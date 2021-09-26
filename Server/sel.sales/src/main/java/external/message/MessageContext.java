package external.message;

public enum MessageContext {
	ORDER("order"),
	MENU("menu");
	
	private final String message;
	
	private MessageContext(String message) {
		this.message = message;
	}
	
	public static MessageContext stringToMessageContext(String serialisedContext) {
		for (MessageContext t : values()) {
			if (t.toString().equals(serialisedContext)) {
				return t;
			}
		}
		throw new IllegalArgumentException("No message type " + serialisedContext);
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
