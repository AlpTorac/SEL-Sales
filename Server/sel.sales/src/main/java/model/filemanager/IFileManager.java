package model.filemanager;

import model.settings.HasSettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeOrderDatas(String datas);
	boolean writeDishMenuData(String data);
	boolean writeSettings(String settings);
	void loadSaved();
	void loadDishMenu(String fileAddress);
	void close();
}
