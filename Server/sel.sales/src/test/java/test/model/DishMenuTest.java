package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;

class DishMenuTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	@BeforeEach
	void startUp() {
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
	void addMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
	}

	@Test
	void removeMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
		
		Assertions.assertEquals(3, data.length);
		
		model.removeMenuItem(menuItemIDFac.createDishMenuItemID("item1"));
		
		data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
		
		Assertions.assertEquals(2, data.length);
		
		model.removeMenuItem(menuItemIDFac.createDishMenuItemID("item2"));
		
		data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
		
		Assertions.assertEquals(1, data.length);
		
		model.removeMenuItem(menuItemIDFac.createDishMenuItemID("item3"));
		
		data = model.getMenuData();
		
		Assertions.assertEquals(0, data.length);
	}
	
	@Test
	void duplicateAddTest() {
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		IDishMenuItemData[] data = model.getMenuData();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
		
		Assertions.assertEquals(3, data.length);
	}
	
	@Test
	void getTest() {
		IDishMenuItem i1 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item1"));
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, "aaa", "item1", BigDecimal.valueOf(2.34), BigDecimal.valueOf(5), BigDecimal.valueOf(4));
		IDishMenuItem i2 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item2"));
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i2, "bbb", "item2", BigDecimal.valueOf(5.67), BigDecimal.valueOf(1), BigDecimal.valueOf(0.5));
		IDishMenuItem i3 = model.getMenuItem(menuItemIDFac.createDishMenuItemID("item3"));
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i3, "ccc", "item3", BigDecimal.valueOf(3.34), BigDecimal.valueOf(4), BigDecimal.valueOf(3.5));
	}
}
