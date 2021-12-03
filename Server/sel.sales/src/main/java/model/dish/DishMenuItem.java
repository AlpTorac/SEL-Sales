package model.dish;

import java.math.BigDecimal;

import model.datamapper.DishMenuItemAttribute;
import model.entity.Entity;
import model.entity.id.EntityID;

public class DishMenuItem extends Entity<DishMenuItemAttribute> {
	DishMenuItem(EntityID id) {
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
	
	public BigDecimal getGrossPricePerPortion() {
		return this.getGrossPrice().divide(this.getPortionSize());
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof DishMenuItem)) {
			return false;
		}
		DishMenuItem castedO = (DishMenuItem) o;
		return this.getDishName().equals(castedO.getDishName()) &&
				this.getID().equals(castedO.getID()) &&
				this.getPortionSize().compareTo(castedO.getPortionSize()) == 0 &&
				this.getGrossPrice().compareTo(castedO.getGrossPrice()) == 0 &&
				this.getProductionCost().compareTo(castedO.getProductionCost()) == 0;
	}
	
	public DishMenuItemData toData() {
		DishMenuItemData data = new DishMenuItemData(this.getID());
		data.setAttributeValue(DishMenuItemAttribute.DISH_NAME, this.getDishName());
		data.setAttributeValue(DishMenuItemAttribute.GROSS_PRICE, this.getGrossPrice());
		data.setAttributeValue(DishMenuItemAttribute.PORTION_SIZE, this.getPortionSize());
		data.setAttributeValue(DishMenuItemAttribute.PRODUCTION_COST, this.getProductionCost());
		return data;
	}
}
