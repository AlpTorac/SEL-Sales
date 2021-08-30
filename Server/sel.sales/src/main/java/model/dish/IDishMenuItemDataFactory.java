package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemDataFactory {
	default IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, String id, IDishMenuItemIDFactory fac) {
		return this.constructData(dishName, portionSize, price, productionCost, fac.createDishMenuItemID(id));
	}
	default IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, IDishMenuItemID id) {
		return this.constructData(dishName, portionSize, price, productionCost, BigDecimal.ZERO, id);
	}
	default IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, String id, IDishMenuItemIDFactory fac) {
		return this.constructData(dishName, portionSize, price, productionCost, discount, fac.createDishMenuItemID(id));
	}
	IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, IDishMenuItemID id);
	default IDishMenuItemData menuItemToData(IDishMenuItem item) {
		return this.constructData(
				item.getDish().getName(),
				item.getPortionSize(),
				item.getPrice(),
				item.getProductionCost(),
				item.getDiscount(),
				item.getID());
	}
}
