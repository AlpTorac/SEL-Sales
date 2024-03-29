package test.model.dish;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.IDishMenuItemData;
import server.model.IServerModel;
import server.model.ServerModel;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuTest {
	private static IServerModel model;
	
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
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void startUp() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	
	@Test
	void addMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}

	@Test
	void removeMenuItemTest() {
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		Assertions.assertEquals(3, data.length);
		
		model.removeMenuItem("item1");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		Assertions.assertEquals(2, data.length);
		
		model.removeMenuItem("item2");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		Assertions.assertEquals(1, data.length);
		
		model.removeMenuItem("item3");
		
		data = model.getMenuData().getAllDishMenuItems();
		
		Assertions.assertEquals(0, data.length);
	}
	
	@Test
	void duplicateAddTest() {
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[0], i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[1], i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data[2], i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		Assertions.assertEquals(3, data.length);
	}
	
	@Test
	void getTest() {
		IDishMenuItemData i1 = model.getMenuItem("item1");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		IDishMenuItemData i2 = model.getMenuItem("item2");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i2, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		IDishMenuItemData i3 = model.getMenuItem("item3");
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(i3, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}
}
