package test.model.dish;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;

public final class DishMenuItemTestUtilityClass {
	public static final MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
	public static void assertMenuItemDataEqual(IDishMenuItemData data, String dishName, String id, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost) {
		Assertions.assertEquals(data.getDishName(), dishName);
		Assertions.assertEquals(data.getID().toString(), id);
		Assertions.assertEquals(data.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(data.getGrossPrice().compareTo(price), 0);
		Assertions.assertEquals(data.getProductionCost().compareTo(productionCost), 0);
	}
	public static void assertMenuItemEqual(IDishMenuItem item, String dishName, String id, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost) {
		Assertions.assertEquals(item.getDishName().getName(), dishName);
		Assertions.assertEquals(item.getID().toString(), id);
		Assertions.assertEquals(item.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(item.getGrossPrice().compareTo(price), 0);
		Assertions.assertEquals(item.getProductionCost().compareTo(productionCost), 0);
	}
	public static void assertMenuItemDataPricesEqual(IDishMenuItemData data, BigDecimal portionSize, BigDecimal grossPrice) {
		Assertions.assertEquals(data.getGrossPrice().compareTo(grossPrice), 0);
		Assertions.assertEquals(data.getGrossPricePerPortion().compareTo(grossPrice.divide(portionSize, mc)), 0);
	}
	public static void assertMenuItemAndDataEqual(IDishMenuItem item, IDishMenuItemData data) {
		Assertions.assertEquals(item.getDishName().getName(), data.getDishName());
		Assertions.assertEquals(item.getID().toString(), data.getID().toString());
		Assertions.assertEquals(item.getPortionSize().compareTo(data.getPortionSize()), 0);
		Assertions.assertEquals(item.getGrossPrice().compareTo(data.getGrossPrice()), 0);
		Assertions.assertEquals(item.getProductionCost().compareTo(data.getProductionCost()), 0);
	}
}
