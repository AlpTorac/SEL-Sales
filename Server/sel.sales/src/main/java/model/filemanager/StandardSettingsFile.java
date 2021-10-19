package model.filemanager;

import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;

public class StandardSettingsFile extends SettingsFile {
	public StandardSettingsFile(String address) {
		super(address, new StandardSettingsSerialiser(), new StandardSettingsParser());
	}
}
