package model.dish;

import java.math.BigDecimal;

import model.datamapper.DishMenuItemAttribute;
import model.entity.IFactory;
import model.entity.id.EntityID;

public class DishMenuItemFactory implements IFactory<DishMenuItemAttribute, DishMenuItem, DishMenuItemData> {
	public DishMenuItemFactory() {
	}

	public DishMenuItem createMenuItem(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, EntityID id) {
		DishMenuItem dmi = new DishMenuItem(id);
		dmi.setAttributeValue(DishMenuItemAttribute.DISH_NAME, dishName);
		dmi.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, portionSize);
		dmi.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, price);
		dmi.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, productionCost);
		return dmi;
	}

	public DishMenuItemData entityToValue(DishMenuItem item) {
		return this.constructData(
				item.getDishName(),
				item.getPortionSize(),
				item.getGrossPrice(),
				item.getProductionCost(),
				item.getID());
	}
	
	public DishMenuItemData constructData(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, EntityID id) {
		DishMenuItemData data = new DishMenuItemData(id);
		data.setAttributeValue(DishMenuItemAttribute.DISH_NAME, dishName);
		data.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, portionSize);
		data.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, price);
		data.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, productionCost);
		return data;
	}
}
