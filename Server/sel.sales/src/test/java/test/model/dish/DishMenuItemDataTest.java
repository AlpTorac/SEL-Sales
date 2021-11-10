package test.model.dish;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import server.model.IServerModel;
import server.model.ServerModel;
@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuItemDataTest {
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
	
	private IDishMenuItemData iData1;
	private IDishMenuItemData iData2;
	private IDishMenuItemData iData3;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	
		iData1 = model.getMenuItem(i1id);
		iData2 = model.getMenuItem(i2id);
		iData3 = model.getMenuItem(i3id);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	@Test
	void getAssociatedMenuItemTest() {
		IDishMenuItemData currentData = model.getMenuItem(i1id);
		IDishMenuItem currentItem = currentData.getAssociatedItem(model.getActiveDishMenuItemFinder());
		
		Assertions.assertTrue(currentData.equals(iData1));
		Assertions.assertTrue(currentItem.equals(iData1.getAssociatedItem(model.getActiveDishMenuItemFinder())));
		
		model.close();
		model = new ServerModel(this.testFolderAddress);
		String newName = "newName";
		BigDecimal newPorSize = BigDecimal.valueOf(9);
		BigDecimal newProCost = BigDecimal.valueOf(10);
		BigDecimal newPrice = BigDecimal.valueOf(50);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(newName,
				i1id,
				newPorSize,
				newProCost,
				newPrice));

		IDishMenuItemData newData = model.getMenuItem(i1id);
		IDishMenuItem newItem = newData.getAssociatedItem(model.getActiveDishMenuItemFinder());
		
		Assertions.assertFalse(newData.equals(iData1));
		Assertions.assertFalse(currentItem.equals(newItem));
		
		Assertions.assertTrue(newData.getAssociatedItem(model.getActiveDishMenuItemFinder())
				.equals(iData1.getAssociatedItem(model.getActiveDishMenuItemFinder())));
	}
	
	@Test
	void compareToTest() {
		Assertions.assertEquals(iData1.compareTo(iData1), iData1.getID().compareTo(iData1.getID()));
		Assertions.assertEquals(iData1.compareTo(iData2), iData1.getID().compareTo(iData2.getID()));
		Assertions.assertEquals(iData1.compareTo(iData3), iData1.getID().compareTo(iData3.getID()));
		
		Assertions.assertEquals(iData2.compareTo(iData1), iData2.getID().compareTo(iData1.getID()));
		Assertions.assertEquals(iData2.compareTo(iData2), iData2.getID().compareTo(iData2.getID()));
		Assertions.assertEquals(iData2.compareTo(iData3), iData2.getID().compareTo(iData3.getID()));
		
		Assertions.assertEquals(iData3.compareTo(iData1), iData3.getID().compareTo(iData1.getID()));
		Assertions.assertEquals(iData3.compareTo(iData2), iData3.getID().compareTo(iData2.getID()));
		Assertions.assertEquals(iData3.compareTo(iData3), iData3.getID().compareTo(iData3.getID()));
	}
	
	@Test
	void equalityTest() {
		Assertions.assertTrue(iData1.equals(iData1));
		Assertions.assertTrue(iData2.equals(iData2));
		Assertions.assertTrue(iData3.equals(iData3));
		
		Assertions.assertFalse(iData1.equals(iData2));
		Assertions.assertFalse(iData1.equals(iData3));
		Assertions.assertFalse(iData2.equals(iData1));
		Assertions.assertFalse(iData2.equals(iData3));
		Assertions.assertFalse(iData3.equals(iData1));
		Assertions.assertFalse(iData3.equals(iData2));
		
		Assertions.assertFalse(iData1.equals(null));
		Assertions.assertFalse(iData1.equals(new Object()));
	}
	
	@Test
	void contentTest() {
		IDishMenuItemData d1 = model.getMenuItem(i1id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		IDishMenuItemData d2 = model.getMenuItem(i2id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d2, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		IDishMenuItemData d3 = model.getMenuItem(i3id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d3, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}
	
	@Test
	void pricesPerPortionTest() {
		IDishMenuItemData[] data = model.getMenuData().getAllDishMenuItems();
		
		IDishMenuItemData d1 = data[0];
		IDishMenuItemData d2 = data[1];
		IDishMenuItemData d3 = data[2];
		
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d1, i1PorSize, i1Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d2, i2PorSize, i2Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d3, i3PorSize, i3Price);
	}

}
