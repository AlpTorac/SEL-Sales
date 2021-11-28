package model.settings;

public enum SettingsField {
	ORDER_FOLDER("orderFolder"),
	DISH_MENU_FOLDER("dishMenuFolder"),
	PING_PONG_TIMEOUT("pingPongTimeout"),
	PING_PONG_MINIMAL_DELAY("pingPongMinimalDelay"),
	PING_PONG_RESEND_LIMIT("pingPongResendLimit"),
	SEND_TIMEOUT("sendTimeout"),
	
	TABLE_NUMBERS("tableNumbers")
	;
	
	private String fieldName;
	
	private SettingsField(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public String toString() {
		return this.fieldName;
	}
	
	public static SettingsField stringToSettingsField(String fieldName) {
		if (fieldName == null) {
			return null;
		} else {
			for (SettingsField sf : SettingsField.values()) {
				if (sf.toString().equals(fieldName)) {
					return sf;
				}
			}
			return null;
		}
	}
}
