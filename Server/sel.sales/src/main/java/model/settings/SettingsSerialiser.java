package model.settings;

public abstract class SettingsSerialiser implements ISettingsSerialiser {
	private ISettingsFormat format;
	
	SettingsSerialiser(ISettingsFormat format) {
		this.format = format;
	}
	
	@Override
	public ISettingsFormat getFormat() {
		return this.format;
	}
}
