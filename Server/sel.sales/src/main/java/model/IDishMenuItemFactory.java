package model;

public interface IDishMenuItemFactory {
	IDishMenuItem createMenuItem(IDishMenuItemData item);
	IDish createDish(String dishName);
}
