package model.dish.serialise;

import java.math.BigDecimal;

public class ExternalDishMenuItemSerialiser implements IDishMenuItemSerialiser {

	private IDishMenuItemFormat format = new ExternalDishMenuItemFormat();
	
	@Override
	public String serialise(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost,
			BigDecimal price) {
		String result = "";
		result += this.serialiseDishName(dishName) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialiseDishID(id) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialisePortionSize(portionSize) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialiseProductionCost(productionCost) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialisePrice(price); //  + this.getDishMenuItemDataFieldSeperator()
		return result;
	}

	@Override
	public IDishMenuItemFormat getDishMenuFormat() {
		return this.format;
	}

}
