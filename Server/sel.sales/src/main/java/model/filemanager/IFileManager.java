package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	boolean writeDeviceDatas(String deviceDatas);
	default void loadSaved() {
		this.loadSavedSettings();
		this.loadSavedDishMenu();
		this.loadSavedKnownDevices();
	}
	void loadSavedSettings();
	void loadSavedDishMenu();
	void loadSavedKnownDevices();
	void loadDishMenu(String fileAddress);
	void loadKnownDevices(String fileAddress);
	void setResourcesFolderAddress(String folderAddress);
	void close();
}
