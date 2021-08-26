package model;

public interface IDishMenuItemDataFactory {
	IDishMenuItemData constructData(String dishName, double portionSize, double price, double productionCost, Object id);

	IDishMenuItemData menuItemToData(IDishMenuItem item);
}
