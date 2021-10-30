package model.dish;

import java.math.BigDecimal;

import model.id.EntityID;

public interface IDishMenuItemFactory {
	IDishMenuItem createMenuItem(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, EntityID id);
	IDish createDish(String dishName);
}
