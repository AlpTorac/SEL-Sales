package model.settings;

public class StandardSettingsParser extends SettingsParser {

	public StandardSettingsParser() {
		super(new StandardSettingsFormat());
	}

	@Override
	public ISettings constructSettings() {
		return new Settings();
	}

}
