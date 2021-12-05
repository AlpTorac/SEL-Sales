package test.model.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import server.model.IServerModel;
import test.FXTestTemplate;

class OrderItemTest extends FXTestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
		this.addOrdersToModel(model);
	}

	@AfterEach
	void cleanUp() {
		this.closeModel(model);
	}

//	@Test
//	void compareToTest() {
//		Assertions.assertEquals(orderItem1.compareTo(orderItem1), orderItem1.getItemData().compareTo(orderItem1.getItemData()));
//		Assertions.assertEquals(orderItem1.compareTo(orderItem2), orderItem1.getItemData().compareTo(orderItem2.getItemData()));
//		Assertions.assertEquals(orderItem1.compareTo(orderItem3), orderItem1.getItemData().compareTo(orderItem3.getItemData()));
//		
//		Assertions.assertEquals(orderItem2.compareTo(orderItem1), orderItem2.getItemData().compareTo(orderItem1.getItemData()));
//		Assertions.assertEquals(orderItem2.compareTo(orderItem2), orderItem2.getItemData().compareTo(orderItem2.getItemData()));
//		Assertions.assertEquals(orderItem2.compareTo(orderItem3), orderItem2.getItemData().compareTo(orderItem3.getItemData()));
//		
//		Assertions.assertEquals(orderItem3.compareTo(orderItem1), orderItem3.getItemData().compareTo(orderItem1.getItemData()));
//		Assertions.assertEquals(orderItem3.compareTo(orderItem2), orderItem3.getItemData().compareTo(orderItem2.getItemData()));
//		Assertions.assertEquals(orderItem3.compareTo(orderItem3), orderItem3.getItemData().compareTo(orderItem3.getItemData()));
//	}
//
//	@Test
//	void portionsInOrderTest() {
//		DishMenuItemData oi1 = orderItem1.getItemData();
//		Assertions.assertEquals(orderItem1.getPortionsInOrder().compareTo(oi1.getPortionSize().multiply(orderItem1.getAmount())), 0);
//		
//		DishMenuItemData oi2 = orderItem2.getItemData();
//		Assertions.assertEquals(orderItem2.getPortionsInOrder().compareTo(oi2.getPortionSize().multiply(orderItem2.getAmount())), 0);
//		
//		DishMenuItemData oi3 = orderItem3.getItemData();
//		Assertions.assertEquals(orderItem3.getPortionsInOrder().compareTo(oi3.getPortionSize().multiply(orderItem3.getAmount())), 0);
//	}
//	
//	@Test
//	void grossPricePerPortionTest() {
//		DishMenuItemData oi1 = orderItem1.getItemData();
//		Assertions.assertEquals(orderItem1.getGrossPricePerPortion().compareTo(oi1.getGrossPricePerPortion()), 0);
//		
//		DishMenuItemData oi2 = orderItem2.getItemData();
//		Assertions.assertEquals(orderItem2.getGrossPricePerPortion().compareTo(oi2.getGrossPricePerPortion()), 0);
//		
//		DishMenuItemData oi3 = orderItem3.getItemData();
//		Assertions.assertEquals(orderItem3.getGrossPricePerPortion().compareTo(oi3.getGrossPricePerPortion()), 0);
//	}
//	
//	@Test
//	void grossPricePerMenuItemTest() {
//		DishMenuItemData oi1 = orderItem1.getItemData();
//		Assertions.assertEquals(orderItem1.getGrossPricePerMenuItem().compareTo(oi1.getGrossPrice()), 0);
//		
//		DishMenuItemData oi2 = orderItem2.getItemData();
//		Assertions.assertEquals(orderItem2.getGrossPricePerMenuItem().compareTo(oi2.getGrossPrice()), 0);
//		
//		DishMenuItemData oi3 = orderItem3.getItemData();
//		Assertions.assertEquals(orderItem3.getGrossPricePerMenuItem().compareTo(oi3.getGrossPrice()), 0);
//	}
	
	@Test
	void contentTest() {
		Assertions.assertTrue(orderItem1.getItem().equals(iData1));
		Assertions.assertTrue(orderItem1.getAmount().compareTo(o1a1) == 0);
		
		Assertions.assertTrue(orderItem2.getItem().equals(iData2));
		Assertions.assertTrue(orderItem2.getAmount().compareTo(o2a2) == 0);
		
		Assertions.assertTrue(orderItem3.getItem().equals(iData3));
		Assertions.assertTrue(orderItem3.getAmount().compareTo(o3a3) == 0);
	}
	
//	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equalityTest() {
		Assertions.assertTrue(orderItem1.equals(orderItem1));
		Assertions.assertTrue(orderItem2.equals(orderItem2));
		Assertions.assertTrue(orderItem3.equals(orderItem3));
		
		Assertions.assertFalse(orderItem1.equals(orderItem2));
		Assertions.assertFalse(orderItem1.equals(orderItem3));
		
		Assertions.assertFalse(orderItem2.equals(orderItem1));
		Assertions.assertFalse(orderItem2.equals(orderItem3));
		
		Assertions.assertFalse(orderItem3.equals(orderItem2));
		Assertions.assertFalse(orderItem3.equals(orderItem1));
		
//		Assertions.assertTrue(orderItem1.equals(fac.constructData(orderItem1.getItemData(), orderItem1.getAmount())));
//		Assertions.assertTrue(orderItem2.equals(fac.constructData(orderItem2.getItemData(), orderItem2.getAmount())));
//		Assertions.assertTrue(orderItem3.equals(fac.constructData(orderItem3.getItemData(), orderItem3.getAmount())));
		
		Assertions.assertFalse(orderItem1.equals(null));
	}
	
//	@Test
//	void combineTest() {
//		AccumulatingOrderItemAggregate sameCombinedOrderItems = orderItem1.combine(orderItem1);
//		Assertions.assertTrue(sameCombinedOrderItems.getItemData().equals(orderItem1.getItemData()));
//		Assertions.assertEquals(sameCombinedOrderItems.getAmount().compareTo(orderItem1.getAmount().add(orderItem1.getAmount())), 0);
//		
//		AccumulatingOrderItemAggregate differentCombinedOrderItems = orderItem1.combine(orderItem2);
//		Assertions.assertTrue(differentCombinedOrderItems.getItemData().equals(orderItem1.getItemData()));
//		Assertions.assertEquals(differentCombinedOrderItems.getAmount().compareTo(orderItem1.getAmount().add(orderItem2.getAmount())), 0);
//		
//		Assertions.assertThrows(IllegalArgumentException.class, ()->{orderItem1.combine(orderItem3);});
//	}
}
