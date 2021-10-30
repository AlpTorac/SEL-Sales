package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public interface IDishMenuItemDataFactory {
	IDishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, EntityID idParameters);
	default IDishMenuItemData menuItemToData(IDishMenuItem item) {
		return this.constructData(
				item.getDish().getName(),
				item.getPortionSize(),
				item.getPrice(),
				item.getProductionCost(),
				item.getID());
	}
}
