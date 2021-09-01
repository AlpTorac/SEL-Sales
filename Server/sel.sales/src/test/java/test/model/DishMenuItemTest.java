package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IDishMenuItemFinder;
import model.IModel;
import model.Model;
import model.dish.Dish;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import test.GeneralTestUtilityClass;

class DishMenuItemTest {

	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	@BeforeEach
	void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				i1Name,
				i1PorSize,
				i1Price,
				i1ProCost,
				i1Disc,
				i1id, menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				i2Name,
				i2PorSize,
				i2Price,
				i2ProCost,
				i2Disc,
				i2id, menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				i3Name,
				i3PorSize,
				i3Price,
				i3ProCost,
				i3Disc,
				i3id, menuItemIDFac));
		
	}
	
	@Test
	void equalityTest() {
		IDishMenuItem i1 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i1id));
		IDishMenuItem i2 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i2id));
		IDishMenuItem i3 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i3id));
		
		Assertions.assertTrue(i1.equals(i1));
		Assertions.assertTrue(i2.equals(i2));
		Assertions.assertTrue(i3.equals(i3));
		
		Assertions.assertFalse(i1.equals(i2));
		Assertions.assertFalse(i1.equals(i3));
		Assertions.assertFalse(i2.equals(i1));
		Assertions.assertFalse(i2.equals(i3));
		Assertions.assertFalse(i3.equals(i1));
		Assertions.assertFalse(i3.equals(i2));
	}
	
	@Test
	void compareTest() {
		IDishMenuItem i1 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i1id));
		IDishMenuItem i2 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i2id));
		IDishMenuItem i3 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i3id));
		
		Assertions.assertEquals(i1.compareTo(i1), 0);
		Assertions.assertEquals(i2.compareTo(i2), 0);
		Assertions.assertEquals(i3.compareTo(i3), 0);
		
		Assertions.assertNotEquals(i1.compareTo(i2), 0);
		Assertions.assertNotEquals(i1.compareTo(i3), 0);
		Assertions.assertNotEquals(i2.compareTo(i1), 0);
		Assertions.assertNotEquals(i2.compareTo(i3), 0);
		Assertions.assertNotEquals(i3.compareTo(i1), 0);
		Assertions.assertNotEquals(i3.compareTo(i2), 0);
	}

	@Test
	void setTest() {
		IDishMenuItem i1 = model.getMenuItem(menuItemIDFac.createDishMenuItemID(i1id));
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		
		BigDecimal newDisc = BigDecimal.valueOf(1);
		i1.setDiscount(newDisc);
		DishMenuItemTestUtilityClass.assertMenuItemDiscountEqual(i1, newDisc);
		String newName = "abc";
		i1.getDish().setName(newName);
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, i1id, i1PorSize, i1Price, i1ProCost);
		String newID = "newItem1";
		i1.setID(menuItemIDFac.createDishMenuItemID(newID));
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, newID, i1PorSize, i1Price, i1ProCost);
		BigDecimal newPorSize = BigDecimal.valueOf(1);
		i1.setPortionSize(newPorSize);
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, newID, newPorSize, i1Price, i1ProCost);
		BigDecimal newPrice = BigDecimal.valueOf(5);
		i1.setPrice(newPrice);
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, newID, newPorSize, newPrice, i1ProCost);
		BigDecimal newProCost = BigDecimal.valueOf(10);
		i1.setProductionCost(newProCost);
		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, newID, newPorSize, newPrice, newProCost);
	}
}
