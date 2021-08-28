package model;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

public interface IModel {
	void addMenuItem(IDishMenuItemData item);
	void removeMenuItem(IDishMenuItemID item);
	void subscribe(Updatable updatable);
	IDishMenuItem getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
	IDishMenuItemData[] getMenuData();
}
