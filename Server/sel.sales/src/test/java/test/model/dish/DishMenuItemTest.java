package test.model.dish;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import server.model.IServerModel;
import server.model.ServerModel;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuItemTest {
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
	
	private DishMenuItemData iData1;
	private DishMenuItemData iData2;
	private DishMenuItemData iData3;
	
	private DishMenuItem i1;
	private DishMenuItem i2;
	private DishMenuItem i3;
	
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
		
		i1 = iData1.getAssociatedItem(model.getActiveDishMenuItemFinder());
		i2 = iData2.getAssociatedItem(model.getActiveDishMenuItemFinder());
		i3 = iData3.getAssociatedItem(model.getActiveDishMenuItemFinder());
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	@Test
	void compareToTest() {
		Assertions.assertEquals(i1.compareTo(i1), i1.getID().compareTo(i1.getID()));
		Assertions.assertEquals(i1.compareTo(i2), i1.getID().compareTo(i2.getID()));
		Assertions.assertEquals(i1.compareTo(i3), i1.getID().compareTo(i3.getID()));
		
		Assertions.assertEquals(i2.compareTo(i1), i2.getID().compareTo(i1.getID()));
		Assertions.assertEquals(i2.compareTo(i2), i2.getID().compareTo(i2.getID()));
		Assertions.assertEquals(i2.compareTo(i3), i2.getID().compareTo(i3.getID()));
		
		Assertions.assertEquals(i3.compareTo(i1), i3.getID().compareTo(i1.getID()));
		Assertions.assertEquals(i3.compareTo(i2), i3.getID().compareTo(i2.getID()));
		Assertions.assertEquals(i3.compareTo(i3), i3.getID().compareTo(i3.getID()));
	}
	
	@Test
	void equalityTest() {
		Assertions.assertTrue(i1.equals(i1));
		Assertions.assertTrue(i2.equals(i2));
		Assertions.assertTrue(i3.equals(i3));
		
		Assertions.assertFalse(i1.equals(i2));
		Assertions.assertFalse(i1.equals(i3));
		Assertions.assertFalse(i2.equals(i1));
		Assertions.assertFalse(i2.equals(i3));
		Assertions.assertFalse(i3.equals(i1));
		Assertions.assertFalse(i3.equals(i2));
		
		Assertions.assertFalse(i1.equals(null));
		Assertions.assertFalse(i1.equals(new Object()));
	}

//	@Test
//	void setTest() {
//		DishMenuItemData i1 = model.getMenuItem(i1id);
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
