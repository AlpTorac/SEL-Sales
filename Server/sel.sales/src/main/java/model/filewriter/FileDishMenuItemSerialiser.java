package model.filewriter;

import java.math.BigDecimal;

import model.dish.serialise.IDishMenuItemFormat;
import model.dish.serialise.IDishMenuItemSerialiser;

public class FileDishMenuItemSerialiser implements IDishMenuItemSerialiser {

	private IDishMenuItemFormat format = new FileDishMenuItemFormat();
	
	@Override
	public String serialise(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost,
			BigDecimal price) {
		String result = "";
		result += this.serialiseDishName(dishName) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialiseDishID(id) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialisePrice(price) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialisePortionSize(portionSize) + this.getDishMenuItemDataFieldSeperator();
		result += this.serialiseProductionCost(productionCost);
		return result;
	}

	@Override
	public IDishMenuItemFormat getDishMenuFormat() {
		return this.format;
	}
}
