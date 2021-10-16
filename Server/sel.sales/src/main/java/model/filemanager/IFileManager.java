package model.filemanager;

import model.dish.IDishMenuData;
import model.order.IOrderData;

public interface IFileManager {
	boolean writeOrderData(IOrderData data);
	boolean writeDishMenuData(IDishMenuData data);
	void setOrderDataFolderAddress(String address);
	void setDishMenuDataFolderAddress(String address);
}
