package model.dish.serialise;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IDishMenuItemSerialiser {
	default String serialise(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost, BigDecimal price, BigDecimal discount) {
		String result = "";
		result += dishName + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += id + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += portionSize.toPlainString() + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += productionCost.toPlainString() + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += price.toPlainString() + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += discount.toPlainString() + this.getDishMenuFormat().getDishMenuItemDataFieldEnd();
		return result;
	}
	
	default String serialise(IDishMenuItemData menuItemData) {
		return this.serialise(
				menuItemData.getDishName(),
				menuItemData.getId(),
				menuItemData.getPortionSize(),
				menuItemData.getProductionCost(),
				menuItemData.getGrossPrice(),
				menuItemData.getDiscount());
	}
	
	IDishMenuFormat getDishMenuFormat();
}
