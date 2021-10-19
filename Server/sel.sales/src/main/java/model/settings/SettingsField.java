package model.settings;

public enum SettingsField {
	ORDER_FOLDER("orderFolder"),
	DISH_MENU_FOLDER("dishMenuFolder");
	
	private String serialisedVersion;
	
	private SettingsField(String serialisedVersion) {
		this.serialisedVersion = serialisedVersion;
	}
	
	@Override
	public String toString() {
		return this.serialisedVersion;
	}
	
	public static SettingsField stringToSettingsField(String serialisedVersion) {
		if (serialisedVersion == null) {
			return null;
		} else {
			for (SettingsField sf : SettingsField.values()) {
				if (sf.toString().equals(serialisedVersion)) {
					return sf;
				}
			}
			return null;
		}
	}
}
