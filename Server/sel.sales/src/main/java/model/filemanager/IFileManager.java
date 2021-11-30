package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeOrderStatusData(String data);
	boolean writeOrderTableNumberData(String data);
	boolean writeOrderData(String data);
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	boolean writeDeviceDatas(String deviceDatas);
	boolean writeOrderNote(String serialiseFor);
	default void loadSaved() {
		this.loadSavedSettings();
		this.loadSavedDishMenu();
		this.loadSavedOrders();
		this.loadSavedKnownDevices();
	}
	void loadSavedOrders();
	void loadSavedSettings();
	void loadSavedDishMenu();
	void loadSavedKnownDevices();
	void loadDishMenu(String fileAddress);
	void loadOrders(String fileAddress);
	void loadKnownDevices(String fileAddress);
	void setResourcesFolderAddress(String folderAddress);
	void close();
}
