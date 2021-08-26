package model;

public class DishMenuItemDataFactory implements IDishMenuItemDataFactory {

	@Override
	public IDishMenuItemData constructData(String dishName, double portionSize, double price, double productionCost,
			Object id) {
		return new DishMenuItemData(dishName, portionSize, price, productionCost, id);
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
