package model.dish;

public interface IDishMenu {
	boolean addMenuItem(IDishMenuItemData item);
	boolean removeMenuItem(String id);
	void editMenuItem(IDishMenuItemData newItem);
	IDishMenuItem getItem(String id);
	IDishMenuItem[] getAllItems();
//	IDishMenuItemData getItem(String id);
//	IDishMenuItemData[] getAllItemData();
}