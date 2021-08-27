package model;

public interface IDishMenuItemDataFactory {
	IDishMenuItemData constructData(String dishName, double portionSize, double price, double productionCost, String id, IDishMenuItemIDFactory fac);

	IDishMenuItemData menuItemToData(IDishMenuItem item);
}
