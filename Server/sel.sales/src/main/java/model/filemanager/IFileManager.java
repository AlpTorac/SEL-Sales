package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeOrderData(String data);
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	boolean writeDeviceDatas(String deviceDatas);
	void loadSaved();
	void loadDishMenu(String fileAddress);
	void loadOrders(String fileAddress);
	void loadKnownDevices(String fileAddress);
	void close();
}
