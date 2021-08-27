package model;

import java.math.BigDecimal;

public interface IDishMenuItem extends Comparable<IDishMenuItem> {

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

	boolean equals(Object o);
}