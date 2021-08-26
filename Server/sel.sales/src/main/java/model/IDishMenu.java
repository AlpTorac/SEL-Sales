package model;

public interface IDishMenu {

	void addMenuItem(IDishMenuItem item);

	void removeMenuItem(IDishMenuItemID id);

	IDishMenuItem getItem(IDishMenuItemID id);

}