package model.settings;

public class StandardSettingsFormat extends SettingsFormat {

	public StandardSettingsFormat() {
		super("", "", "", "[|]", ";" + System.lineSeparator());
	}
	@Override
	public String getSettingsFieldDescriptionSeparatorForString() {
		return "|";
	}
}
