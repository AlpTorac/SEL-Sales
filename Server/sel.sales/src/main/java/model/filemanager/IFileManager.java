package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	boolean writeDeviceDatas(String deviceDatas);
	boolean writeOrderDatas(String orderDatas);
	default void loadSaved() {
		this.loadSavedSettings();
		this.loadSavedDishMenu();
		this.loadSavedOrders();
		this.loadSavedKnownDevices();
	}
	void loadSavedSettings();
	void loadSavedDishMenu();
	void loadSavedOrders();
	void loadSavedKnownDevices();
	void loadDishMenu(String fileAddress);
	void loadKnownDevices(String fileAddress);
	void loadOrders(String fileAddress);
	void setResourcesFolderAddress(String folderAddress);
	void close();
}
