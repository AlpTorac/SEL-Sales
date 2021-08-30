package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

class DishMenuItemTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	@BeforeAll
	static void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
	}
	
	@Test
	void test() {
		IDishMenuItemData[] data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
	}

}
