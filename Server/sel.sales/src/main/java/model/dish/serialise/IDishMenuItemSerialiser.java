package model.dish.serialise;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IDishMenuItemSerialiser {
	String serialise(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost, BigDecimal price, BigDecimal discount);
	
	default String getDishMenuItemDataFieldSeperator() {
		return this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
	}
	
	default String getDishMenuItemDataFieldEnd() {
		return this.getDishMenuFormat().getDishMenuItemDataFieldEnd();
	}
	
	default String serialiseDishName(String dishName) {
		return dishName;
	}
	
	default String serialiseDishID(String id) {
		return id;
	}
	
	default String serialisePortionSize(BigDecimal portionSize) {
		return this.serialiseBigDecimal(portionSize);
	}
	
	default String serialiseProductionCost(BigDecimal productionCost) {
		return this.serialiseBigDecimal(productionCost);
	}
	
	default String serialisePrice(BigDecimal price) {
		return this.serialiseBigDecimal(price);
	}
	
	default String serialiseDiscount(BigDecimal discount) {
		return this.serialiseBigDecimal(discount);
	}
	
	default String serialiseBigDecimal(BigDecimal bd) {
		return bd.toPlainString();
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
	
	IDishMenuItemFormat getDishMenuFormat();
}
