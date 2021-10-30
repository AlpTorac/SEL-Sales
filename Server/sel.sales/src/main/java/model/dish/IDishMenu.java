package model.dish;

import model.id.EntityID;

public interface IDishMenu {
	boolean addMenuItem(IDishMenuItem item);
	boolean removeMenuItem(String id);
	void editMenuItem(IDishMenuItemData newItem);
	IDishMenuItem getItem(String id);
	IDishMenuItem getItem(EntityID id);
	IDishMenuItem[] getAllItems();
//	IDishMenuItemData getItem(String id);
//	IDishMenuItemData[] getAllItemData();
}