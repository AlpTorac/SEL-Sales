package model.settings;

public class SettingsFactory implements ISettingsFactory {

	@Override
	public ISettings createSettings() {
		return new Settings();
	}

}
