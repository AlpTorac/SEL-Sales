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
//		result += this.serialiseDishName(dishName) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
//		result += this.serialiseDishID(id) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
//		result += this.serialisePrice(price) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
//		result += this.serialisePortionSize(portionSize) + this.getDishMenuFormat().getDishMenuItemDataFieldSeperator();
//		result += this.serialiseProductionCost(productionCost);
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
