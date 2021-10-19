package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItemDataFactory {
	IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, String id);
	default IDishMenuItemData menuItemToData(IDishMenuItem item) {
		return this.constructData(
				item.getDish().getName(),
				item.getPortionSize(),
				item.getPrice(),
				item.getProductionCost(),
				item.getID());
	}
}
