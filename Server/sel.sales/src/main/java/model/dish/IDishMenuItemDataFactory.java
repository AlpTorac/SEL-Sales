package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemDataFactory {
	default IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, String id) {
		return this.constructData(dishName, portionSize, price, productionCost, BigDecimal.ZERO, id);
	}
	IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, BigDecimal discount, String id);
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
