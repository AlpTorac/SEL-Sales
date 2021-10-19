package model.settings;

public interface ISettingsFormat {
	String getSettingsFieldStart();
	String getSettingsFieldEnd();
	String getSettingsFieldDescriptionSeparator();
	String getSettingsFileStart();
	String getSettingsFileEnd();
	
	String getSettingsFieldStartForString();
	String getSettingsFieldEndForString();
	String getSettingsFieldDescriptionSeparatorForString();
	String getSettingsFileStartForString();
	String getSettingsFileEndForString();
}
