package model.settings;

public interface ISettings {
	static final String PLACEHOLDER = "";
	default String getPlaceholder() {
		return PLACEHOLDER;
	}
	default void initAllSettingsFields() {
		for (SettingsField sf : SettingsField.values()) {
			if (!this.settingExists(sf)) {
				this.addSetting(sf, this.getPlaceholder());
			}
		}
	}
	default void addAllSettings(String[][] settings) {
		if (settings == null) {
			return;
		}
		for (String[] setting : settings) {
			this.addSetting(setting[0], setting[1]);
		}
	}
	default void addSetting(String serialisedDescription, String serialisedValue) {
		this.addSetting(SettingsField.stringToSettingsField(serialisedDescription), serialisedValue);
	}
	void addSetting(SettingsField description, String serialisedValue);
	String getSetting(SettingsField description);
	void removeSetting(SettingsField description);
	default void changeSettingValue(SettingsField description, String serialisedNewValue) {
		this.removeSetting(description);
		this.addSetting(description, serialisedNewValue);
	}
	String[][] getAllSettings();
	boolean equals(Object o);
	boolean isEmpty();
	default boolean settingExists(SettingsField description) {
		return this.getSetting(description) != null;
	}
}
