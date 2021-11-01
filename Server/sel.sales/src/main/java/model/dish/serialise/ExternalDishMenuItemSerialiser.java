package model.dish.serialise;

import java.math.BigDecimal;

public class ExternalDishMenuItemSerialiser implements IDishMenuItemSerialiser {

	private IDishMenuItemFormat format = new ExternalDishMenuItemFormat();
	
	@Override
	public String serialise(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost,
			BigDecimal price) {
		String result = "";
		result += this.serialiseDishName(dishName) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += this.serialiseDishID(id) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += this.serialisePortionSize(portionSize) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += this.serialiseProductionCost(productionCost) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
		result += this.serialisePrice(price); //  + this.getDishMenuItemDataFieldSeperator()
		return result;
	}

	@Override
	public IDishMenuItemFormat getDishMenuFormat() {
		return this.format;
	}

}
