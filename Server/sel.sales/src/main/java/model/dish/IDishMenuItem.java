package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public interface IDishMenuItem extends Comparable<IDishMenuItem> {
	IDish getDish();
	BigDecimal getPortionSize();
	void setPortionSize(BigDecimal portionSize);
	BigDecimal getPrice();
	void setPrice(BigDecimal price);
	EntityID getID();
	BigDecimal getProductionCost();
	void setProductionCost(BigDecimal productionCost);
	
	boolean equals(Object o);
	default public int compareTo(IDishMenuItem o) {
		return this.getID().compareTo(o.getID());
	}
}