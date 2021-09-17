package external;

public enum MessageType {
	INCOMING_ORDER("incOrder"),
	MENU_ACKNOWLEDGEMENT("menuAck"),
	MENU_REQUEST("menuReq");
	
	private final String message;
	
	private MessageType(String message) {
		this.message = message;
	}
	
	public static MessageType stringToMessageType(String message) {
		for (MessageType t : values()) {
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
