package model.dish;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import model.datamapper.menu.DishMenuItemAttribute;
import model.entity.ValueObject;
import model.entity.id.EntityID;

public class DishMenuItemData extends ValueObject<DishMenuItemAttribute> {
	DishMenuItemData(EntityID id) {
		super(id);
	}
	
	public String getDishName() {
		return (String) this.getAttributeValue(DishMenuItemAttribute.DISH_NAME);
	}
	
	public BigDecimal getProductionCost() {
		return (BigDecimal) this.getAttributeValue(DishMenuItemAttribute.PRODUCTION_COST);
	}
	
	public BigDecimal getGrossPrice() {
		return (BigDecimal) this.getAttributeValue(DishMenuItemAttribute.GROSS_PRICE);
	}
	
	public BigDecimal getPortionSize() {
		return (BigDecimal) this.getAttributeValue(DishMenuItemAttribute.PORTION_SIZE);
	}
	
//	public void setDishName(String name) {
//		this.setAttributeValue(DishMenuItemAttribute.DISH_NAME, name);
//	}
//	
//	public void setProductionCost(BigDecimal prodCost) {
//		this.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, prodCost);
//	}
//	
//	public void setGrossPrice(BigDecimal price) {
//		this.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, price);
//	}
//	
//	public void setPortionSize(BigDecimal porSize) {
//		this.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, porSize);
//	}
	
	public DishMenuItem getAssociatedItem(IDishMenuItemFinder finder) {
		return finder.getMenuItem(this.getID());
	}
	
	@Override
	public String toString() {
		return this.getID().toString();
	}

	@Override
	public DishMenuItemAttribute[] getAllAttributeEnumValues() {
		return DishMenuItemAttribute.values();
	}
}
