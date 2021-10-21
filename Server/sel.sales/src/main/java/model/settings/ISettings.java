package model.settings;

public interface ISettings {
	static final String PLACEHOLDER = "";
	default String getPlaceholder() {
		return PLACEHOLDER;
	}
	default void initAllSettingsFields() {
		for (SettingsField sf : SettingsField.values()) {
			this.addSetting(sf, this.getPlaceholder());
		}
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
	boolean settingExists(SettingsField description);
}