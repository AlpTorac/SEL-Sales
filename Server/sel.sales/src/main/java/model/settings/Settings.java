package model.settings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Settings implements ISettings {

	private Map<SettingsField, String> settings;
	
	public Settings() {
		this.settings = new ConcurrentHashMap<SettingsField, String>();
	}
	
	protected boolean valueExists(String value) {
		return value == null || value.equals(this.getPlaceholder());
	}
	
	@Override
	public void addSetting(SettingsField description, String serialisedValue) {
		String value = serialisedValue;
		if (value == null) {
			value = this.getPlaceholder();
		}
		this.settings.put(description, value);
	}

	@Override
	public void removeSetting(SettingsField description) {
		if (this.settings.containsKey(description)) {
			this.settings.remove(description);
		}
	}

	@Override
	public String[][] getAllSettings() {
		return this.settings.entrySet().stream()
				.sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
				.map(e -> {return new String[] {e.getKey().toString(), e.getValue()};})
				.toArray(String[][]::new);
	}

	@Override
	public String getSetting(SettingsField description) {
		String result = this.settings.get(description);
		return this.valueExists(result) ? null : result;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ISettings)) {
			return false;
		}
		ISettings castedO = (ISettings) o;
		String[][] settings = this.getAllSettings();
		String[][] oSettings = castedO.getAllSettings();
		for (int i = 0; i < settings.length; i++) {
			for (int j = 0; j < settings[0].length; j++) {
//				if (settings[i][j] == oSettings[i][j]) {
//					continue;
//				}
//				if (settings[i][j] == null ^ oSettings[i][j] == null) {
//					return false;
//				}
				if (!settings[i][j].equals(oSettings[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.settings.isEmpty() || this.settings.values().stream().allMatch(i -> this.valueExists(i));
	}

	@Override
	public boolean settingExists(SettingsField description) {
		return this.getSetting(description) != null;
	}
}
