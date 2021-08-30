package model.dish;

public class DishMenuItemFactory implements IDishMenuItemFactory {

	public IDish createDish(String dishName) {
		return new Dish(dishName);
	}

	@Override
	public IDishMenuItem createMenuItem(IDishMenuItemData item) {
		return new DishMenuItem(this.createDish(item.getDishName()), item.getPortionSize(), item.getGrossPrice(), item.getProductionCost(), item.getDiscount(), item.getId());
	}
}
