package model.dish;

import java.math.BigDecimal;

public interface IDishMenuItem extends Comparable<IDishMenuItem> {
	IDish getDish();
	void setDish(IDish dish);
	BigDecimal getPortionSize();
	void setPortionSize(BigDecimal portionSize);
	BigDecimal getPrice();
	void setPrice(BigDecimal price);
	String getID();
	BigDecimal getProductionCost();
	void setProductionCost(BigDecimal productionCost);
	
	boolean equals(Object o);
	default public int compareTo(IDishMenuItem o) {
		return this.getID().compareTo(o.getID());
	}
}