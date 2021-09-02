package model.dish;

import java.math.BigDecimal;

public class DishMenuItemDataFactory implements IDishMenuItemDataFactory {
	@Override
	public IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, BigDecimal discount, IDishMenuItemID id) {
		return new DishMenuItemData(dishName, portionSize, price, productionCost, discount, id);
	}
}