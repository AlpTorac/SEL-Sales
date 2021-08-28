package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemDataFactory {
	default IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, String id, IDishMenuItemIDFactory fac) {
		return this.constructData(dishName, portionSize, price, productionCost, fac.createDishMenuItemID(id));
	}
	IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id);

	default IDishMenuItemData menuItemToData(IDishMenuItem item) {
		return this.constructData(
				item.getDish().getName(),
				item.getPortionSize(),
				item.getPrice(),
				item.getProductionCost(),
				item.getID());
	}
}
