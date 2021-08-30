package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItem extends Comparable<IDishMenuItem> {
	
	default IDishMenuItemData getDishMenuItemData(IDishMenuItemDataFactory fac) {
		return fac.menuItemToData(this);
	}
	IDish getDish();
	void setDish(IDish dish);
	BigDecimal getPortionSize();
	void setPortionSize(BigDecimal portionSize);
	BigDecimal getPrice();
	void setPrice(BigDecimal price);
	IDishMenuItemID getID();
	void setID(IDishMenuItemID id);
	BigDecimal getProductionCost();
	void setProductionCost(BigDecimal productionCost);
	BigDecimal getDiscount();
	void setDiscount(BigDecimal discount);
	
	boolean equals(Object o);
	default public int compareTo(IDishMenuItem o) {
		return this.getID().getID().compareTo(o.getID().getID());
	}
}