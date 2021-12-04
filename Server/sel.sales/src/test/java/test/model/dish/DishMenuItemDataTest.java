package test.model.dish;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import server.model.IServerModel;
import test.TestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuItemDataTest extends TestTemplate {
	private static IServerModel model;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	@Test
	void getAssociatedMenuItemTest() {
		DishMenuItemData currentData = model.getMenuItem(i1id);
		DishMenuItem currentItem = currentData.getAssociatedItem(model.getActiveDishMenuItemFinder());
		
		Assertions.assertTrue(currentData.equals(iData1));
		Assertions.assertTrue(currentItem.equals(iData1.getAssociatedItem(model.getActiveDishMenuItemFinder())));
		
		model.close();
		model = this.createMinimalServerModel();
		String newName = "newName";
		BigDecimal newPorSize = BigDecimal.valueOf(9);
		BigDecimal newProCost = BigDecimal.valueOf(10);
		BigDecimal newPrice = BigDecimal.valueOf(50);
		this.addMenuItemToServerModel(model, newName,
				i1id,
				newPorSize,
				newPrice,
				newProCost);

		DishMenuItemData newData = model.getMenuItem(i1id);
		DishMenuItem newItem = newData.getAssociatedItem(model.getActiveDishMenuItemFinder());
		
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
		DishMenuItemData d1 = model.getMenuItem(i1id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d1, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		DishMenuItemData d2 = model.getMenuItem(i2id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d2, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		DishMenuItemData d3 = model.getMenuItem(i3id);
		DishMenuItemTestUtilityClass.assertMenuItemDataEqual(d3, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
	}
	
	@Test
	void pricesPerPortionTest() {
		DishMenuItemData[] data = model.getMenuData().getAllItems().toArray(DishMenuItemData[]::new);
		
		DishMenuItemData d1 = data[0];
		DishMenuItemData d2 = data[1];
		DishMenuItemData d3 = data[2];
		
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d1, i1PorSize, i1Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d2, i2PorSize, i2Price);
		DishMenuItemTestUtilityClass.assertMenuItemDataPricesEqual(d3, i3PorSize, i3Price);
	}

}
