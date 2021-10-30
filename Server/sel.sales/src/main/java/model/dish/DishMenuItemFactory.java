package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public class DishMenuItemFactory implements IDishMenuItemFactory {
	public DishMenuItemFactory() {
	}
	
	public IDish createDish(String dishName) {
		return new Dish(dishName);
	}

	@Override
	public IDishMenuItem createMenuItem(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, EntityID id) {
		return new DishMenuItem(this.createDish(dishName), portionSize, price, productionCost, id);
	}
}
