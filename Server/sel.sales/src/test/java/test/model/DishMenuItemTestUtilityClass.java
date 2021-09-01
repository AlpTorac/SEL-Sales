package test.model;

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
		Assertions.assertEquals(data.getId().toString(), id);
		Assertions.assertEquals(data.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(data.getGrossPrice().compareTo(price), 0);
		Assertions.assertEquals(data.getProductionCost().compareTo(productionCost), 0);
	}
	public static void assertMenuItemEqual(IDishMenuItem item, String dishName, String id, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost) {
		Assertions.assertEquals(item.getDish().getName(), dishName);
		Assertions.assertEquals(item.getID().toString(), id);
		Assertions.assertEquals(item.getPortionSize().compareTo(portionSize), 0);
		Assertions.assertEquals(item.getPrice().compareTo(price), 0);
		Assertions.assertEquals(item.getProductionCost().compareTo(productionCost), 0);
	}
	public static void assertMenuItemDataPricesEqual(IDishMenuItemData data, BigDecimal discount, BigDecimal portionSize, BigDecimal grossPrice) {
		Assertions.assertEquals(data.getDiscount(), discount);
		Assertions.assertEquals(data.getGrossPrice(), grossPrice);
		Assertions.assertEquals(data.getGrossPricePerPortion(), grossPrice.divide(portionSize, mc));
		BigDecimal netPrice = grossPrice.subtract(discount);
		Assertions.assertEquals(data.getNetPrice(), netPrice);
		Assertions.assertEquals(data.getNetPricePerPortion(), netPrice.divide(portionSize, mc));
	}
	public static void assertMenuItemDiscountEqual(IDishMenuItem item, BigDecimal discount) {
		Assertions.assertEquals(item.getDiscount(), discount);
	}
}
