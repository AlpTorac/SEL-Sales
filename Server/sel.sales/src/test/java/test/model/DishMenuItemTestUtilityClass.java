package test.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;

import model.dish.IDishMenuItemData;

public final class DishMenuItemTestUtilityClass {
	public static void assertMenuItemDataEqual(IDishMenuItemData data, String dishName, String id, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost) {
		Assertions.assertEquals(data.getDishName(), dishName);
		Assertions.assertEquals(data.getId().getID(), id);
		Assertions.assertEquals(data.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(data.getGrossPrice().compareTo(price), 0);
		Assertions.assertEquals(data.getProductionCost().compareTo(productionCost), 0);
	}
}
