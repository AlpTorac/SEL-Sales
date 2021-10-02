package external.message;

public enum MessageContext {
	ORDER("order"),
	MENU("menu"),
	PINGPONG("pingpong");
	
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
		return null;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
