package test.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.dish.serialise.IDishMenuItemSerialiser;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuTest {
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
	void addMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost, i1Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost, i2Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
	}

	@Test
	void removeMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost, i1Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost, i2Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
		
		Assertions.assertEquals(3, data.length);
		
		model.removeMenuItem("item1");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i2Name, i2id, i2PorSize, i2Price, i2ProCost, i2Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
		
		Assertions.assertEquals(2, data.length);
		
		model.removeMenuItem("item2");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
		
		Assertions.assertEquals(1, data.length);
		
		model.removeMenuItem("item3");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		Assertions.assertEquals(0, data.length);
	}
	
	@Test
	void duplicateAddTest() {
		model.addMenuItem(serialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
		
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost, i1Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost, i2Disc);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
		
		Assertions.assertEquals(3, data.length);
	}
	
	@Test
	void getTest() {
		IDishMenuItemData i1 = model.getMenuItem("item1");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost, i1Disc);
		IDishMenuItemData i2 = model.getMenuItem("item2");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i2, i2Name, i2id, i2PorSize, i2Price, i2ProCost, i2Disc);
		IDishMenuItemData i3 = model.getMenuItem("item3");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i3, i3Name, i3id, i3PorSize, i3Price, i3ProCost, i3Disc);
	}
}
