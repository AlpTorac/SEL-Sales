package model.filemanager;

import model.dish.IDishMenuData;
import model.order.IOrderData;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.SettingsField;

public interface IFileManager extends HasSettingsField {
	boolean writeOrderDatas(IOrderData[] datas);
	boolean writeDishMenuData(IDishMenuData data);
	boolean writeSettings(ISettings settings);
	void loadSaved();
}
