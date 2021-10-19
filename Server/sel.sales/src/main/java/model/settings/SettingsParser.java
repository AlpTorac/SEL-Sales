package model.settings;

public abstract class SettingsParser implements ISettingsParser {
	
	private ISettingsFormat format;
	
	SettingsParser(ISettingsFormat format) {
		this.format = format;
	}

	@Override
	public ISettingsFormat getFormat() {
		return this.format;
	}
}
