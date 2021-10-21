package model.filemanager;

import model.filewriter.FileAccess;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;

public abstract class SettingsFile extends FileAccess {
	private final static String defaultName = "settings";
	
	public SettingsFile(String address) {
		super(address);
	}

	@Override
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
	public boolean writeToFile(String s) {
		this.remakeFile();
		return super.writeToFile(s);
	}
}
