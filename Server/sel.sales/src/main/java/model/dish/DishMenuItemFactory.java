package model.dish;

import java.math.BigDecimal;

import model.datamapper.menu.DishMenuItemAttribute;
import model.entity.IFactory;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;

public class DishMenuItemFactory implements IFactory<DishMenuItemAttribute, DishMenuItem, DishMenuItemData> {
	private EntityIDFactory idFac = new MinimalIDFactory();
	DishMenuItem createMenuItem(String dishName, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost, EntityID id) {
		DishMenuItem dmi = new DishMenuItem(id);
		dmi.setAttributeValue(DishMenuItemAttribute.DISH_NAME, dishName);
		dmi.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, portionSize);
		dmi.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, price);
		dmi.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, productionCost);
		return dmi;
	}
	
	@Override
	public DishMenuItemData entityToValue(DishMenuItem item) {
		DishMenuItemData data = this.constructMinimalValueObject(item.getID());
		data.setAttributesSameAs(item);
		return data;
//		return this.constructData(
//				item.getDishName(),
//				item.getPortionSize(),
//				item.getGrossPrice(),
//				item.getProductionCost(),
//				item.getID());
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
	
	public DishMenuItemData constructData(String dishName, String id, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost) {
		DishMenuItemData data = new DishMenuItemData(this.idFac.createID(id));
		data.setAttributeValue(DishMenuItemAttribute.DISH_NAME, dishName);
		data.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, portionSize);
		data.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, price);
		data.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, productionCost);
		return data;
	}

	@Override
	public DishMenuItem valueToEntity(DishMenuItemData valueObject) {
		DishMenuItem item = new DishMenuItem(valueObject.getID());
//		item.setAttributeValue(DishMenuItemAttribute.DISH_NAME, valueObject.getDishName());
//		item.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, valueObject.getPortionSize());
//		item.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, valueObject.getGrossPrice());
//		item.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, valueObject.getProductionCost());
		item.setAttributesSameAs(valueObject);
		return item;
	}

	@Override
	public DishMenuItemData constructMinimalValueObject(EntityID id) {
		return new DishMenuItemData(id);
	}
}
