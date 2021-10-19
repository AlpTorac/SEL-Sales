package model.filemanager;

import model.filewriter.FileAccess;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;

public abstract class SettingsFile extends FileAccess {
	private final static String defaultName = "settings";
	private ISettingsSerialiser serialiser;
	private ISettingsParser parser;
	
	public SettingsFile(String address, ISettingsSerialiser serialiser, ISettingsParser parser) {
		super(address);
		this.serialiser = serialiser;
		this.parser = parser;
	}

	@Override
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
	public boolean writeSettings(ISettings s) {
		this.remakeFile();
		return this.writeToFile(serialiser.serialise(s));
	}
	
	public ISettings loadSettings() {
		if (this.readFile() != null && this.readFile() != "") {
			return this.parser.parseSettings(this.readFile());
		}
		return null;
	}
}
