package model.settings;

public interface ISettingsParser {
	default ISettings parseSettings(String readFile) {
		ISettings s = this.constructSettings();
		String serialisedFields = this.getSerialisedSettingsFields(readFile);
		String[] separatedFields = this.separateFields(serialisedFields);
		String[] fieldData = this.removeFieldStarts(separatedFields);
		String[][] fields = this.getSerialisedSettingsData(fieldData);
		for (String[] f : fields) {
			if (f.length > 1) {
				this.addSetting(s, f[0], f[1]);
			}
		}
		return s;
	}
	
	/**
	 * Splits the fields by removing their end
	 */
	default String[] separateFields(String serialisedSettingsFields) {
		return serialisedSettingsFields.split(this.getFormat().getSettingsFieldEnd());
	}
	
	/**
	 * Removes the start and the end of the file
	 */
	default String getSerialisedSettingsFields(String ss) {
		int begin = 0;
		int end = ss.length();
		
		if (ss.startsWith(this.getFormat().getSettingsFileStart())) {
			begin = this.getFormat().getSettingsFileStart().length();
		}
		if (ss.endsWith(this.getFormat().getSettingsFileEnd())) {
			end -= this.getFormat().getSettingsFileEnd().length();
		}
		
		return ss.substring(begin, end);
	}
	/**
	 * Removes the start of the settings fields
	 */
	default String[] removeFieldStarts(String[] serialisedFieldsWithStart) {
		String[] result = new String[serialisedFieldsWithStart.length];
		for (int i = 0; i < result.length; i++) {
			if (serialisedFieldsWithStart[i].startsWith(this.getFormat().getSettingsFieldStart())) {
				result[i] = serialisedFieldsWithStart[i].substring(this.getFormat().getSettingsFieldStart().length());
			}
		}
		return result;
	}
	default void addSetting(ISettings s, String description, String serialisedValue) {
		s.changeSettingValue(SettingsField.stringToSettingsField(description), serialisedValue);
	}
	/**
	 * Gets the description and the value of the settings fields
	 */
	default String[][] getSerialisedSettingsData(String[] fieldData) {
		String[][] result = new String[fieldData.length][];
		for (int i = 0; i < fieldData.length; i++) {
			result[i] = fieldData[i].split(this.getFormat().getSettingsFieldDescriptionSeparator());
		}
		return result;
	}
	ISettings constructSettings();
	
	ISettingsFormat getFormat();
}
