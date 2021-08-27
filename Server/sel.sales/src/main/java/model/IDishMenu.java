package model;

public interface IDishMenu {

	boolean addMenuItem(IDishMenuItem item);

	boolean removeMenuItem(IDishMenuItemID id);

	IDishMenuItem getItem(IDishMenuItemID id);

	IDishMenuItem[] getAllItems();
	
}