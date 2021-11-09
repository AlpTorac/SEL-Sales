package model.filemanager;

import model.filewriter.FileAccess;

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
}
