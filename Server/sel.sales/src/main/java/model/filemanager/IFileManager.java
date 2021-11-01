package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeOrderData(String data);
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	boolean writeClientDatas(String clientDatas);
	void loadSaved();
	void loadDishMenu(String fileAddress);
	void loadOrders(String fileAddress);
	void loadKnownClients(String fileAddress);
	void close();
}
