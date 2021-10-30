package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public class DishMenuItemDataFactory implements IDishMenuItemDataFactory {
	@Override
	public IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, EntityID id) {
		return new DishMenuItemData(dishName, portionSize, price, productionCost, id);
	}
}
