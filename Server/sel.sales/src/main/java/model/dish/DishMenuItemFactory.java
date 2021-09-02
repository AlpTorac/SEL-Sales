package model.dish;

public class DishMenuItemFactory implements IDishMenuItemFactory {
	private IDishMenuItemIDFactory idFac;
	
	public DishMenuItemFactory(IDishMenuItemIDFactory idFac) {
		this.idFac = idFac;
	}
	
	public IDish createDish(String dishName) {
		return new Dish(dishName);
	}

	@Override
	public IDishMenuItem createMenuItem(IDishMenuItemData item) {
		return new DishMenuItem(this.createDish(item.getDishName()), item.getPortionSize(), item.getGrossPrice(), item.getProductionCost(), item.getDiscount(), idFac.createDishMenuItemID(item.getId()));
	}
}
