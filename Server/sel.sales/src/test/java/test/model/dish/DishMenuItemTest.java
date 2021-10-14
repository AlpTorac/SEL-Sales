package test.model.dish;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.Dish;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.dish.IDishMenuItemIDFactory;
import model.dish.serialise.IDishMenuItemSerialiser;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuItemTest {
	private static IModel model;
	private static IDishMenuItemSerialiser serialiser;
	
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
		serialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(serialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price, i1Disc));
		model.addMenuItem(serialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price, i2Disc));
		model.addMenuItem(serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
	}
	
	@Test
	void equalityTest() {
		IDishMenuItemData i1 = model.getMenuItem(i1id);
		IDishMenuItemData i2 = model.getMenuItem(i2id);
		IDishMenuItemData i3 = model.getMenuItem(i3id);
		
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
		IDishMenuItemData i1 = model.getMenuItem(i1id);
		IDishMenuItemData i2 = model.getMenuItem(i2id);
		IDishMenuItemData i3 = model.getMenuItem(i3id);
		
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

//	@Test
//	void setTest() {
//		IDishMenuItemData i1 = model.getMenuItem(i1id);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost, i1Disc);
//		
//		BigDecimal newDisc = BigDecimal.valueOf(1);
//		i1.setDiscount(newDisc);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost, newDisc);
//		String newName = "abc";
//		i1.getDish().setName(newName);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, i1id, i1PorSize, i1Price, i1ProCost, newDisc);
//		BigDecimal newPorSize = BigDecimal.valueOf(1);
//		i1.setPortionSize(newPorSize);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, i1id, newPorSize, i1Price, i1ProCost, newDisc);
//		BigDecimal newPrice = BigDecimal.valueOf(5);
//		i1.setPrice(newPrice);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, i1id, newPorSize, newPrice, i1ProCost, newDisc);
//		BigDecimal newProCost = BigDecimal.valueOf(10);
//		i1.setProductionCost(newProCost);
//		DishMenuItemTestUtilityClass.assertMenuItemEqual(i1, newName, i1id, newPorSize, newPrice, newProCost, newDisc); 
//	}
}
