package test.model.dish;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.IServerModel;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class DishMenuItemTest extends FXTestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		this.closeModel(model);
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
