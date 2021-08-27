package model;

public class DishMenuItemDataFactory implements IDishMenuItemDataFactory {

	@Override
	public IDishMenuItemData constructData(String dishName, double portionSize, double price, double productionCost,
			String id, IDishMenuItemIDFactory fac) {
		return new DishMenuItemData(dishName, portionSize, price, productionCost, fac.createDishMenuItemID(id));
	}

	@Override
	public IDishMenuItemData menuItemToData(IDishMenuItem item) {
		return new DishMenuItemData(
				item.getDish().getName(),
				item.getPortionSize().doubleValue(),
				item.getPrice().doubleValue(),
				item.getProductionCost().doubleValue(),
				item.getID()
			);
	}
}
