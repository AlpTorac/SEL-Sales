package model;

import java.math.BigDecimal;

public interface IDishMenuItem {

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

}