package test.model.order;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dish.IDishMenuItemData;
import model.order.AccumulatingOrderItemAggregate;
import model.order.AccumulatingOrderItemAggregate;
import model.order.AccumulatingOrderItemAggregate;
import model.order.AccumulatingOrderItemAggregate;
import model.order.AccumulatingOrderItemAggregate;
import server.model.IServerModel;
import server.model.ServerModel;

class OrderItemTest {

	private static IServerModel model;
	
	private IDishMenuItemData item1;
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private String i1id = "item1";
	
	private IDishMenuItemData item2;
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private String i2id = "item2";
	
	private IDishMenuItemData item3;
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private String i3id = "item3";
	
	private BigDecimal orderItem1a = BigDecimal.valueOf(2);
	private BigDecimal orderItem2a = BigDecimal.valueOf(3);
	private BigDecimal orderItem3a = BigDecimal.valueOf(5);
	
	private AccumulatingOrderItemAggregate orderItem1;
	private AccumulatingOrderItemAggregate orderItem2;
	private AccumulatingOrderItemAggregate orderItem3;
	
	private AccumulatingOrderItemAggregate orderItemData1;
	private AccumulatingOrderItemAggregate orderItemData2;
	private AccumulatingOrderItemAggregate orderItemData3;
	
	private OrderItemFactory fac = new OrderItemFactory();
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		item1 = model.getMenuItem(i1id);
		item2 = model.getMenuItem(i2id);
		item3 = model.getMenuItem(i3id);
		
		orderItem1 = new AccumulatingOrderItemAggregate(item1, orderItem1a);
		orderItem2 = new AccumulatingOrderItemAggregate(item1, orderItem2a);
		orderItem3 = new AccumulatingOrderItemAggregate(item2, orderItem3a);
		
		orderItemData1 = fac.orderItemToData(orderItem1);
		orderItemData2 = fac.orderItemToData(orderItem2);
		orderItemData3 = fac.orderItemToData(orderItem3);
	}

	@AfterEach
	void cleanUp() {
		model.close();
	}

	@Test
	void compareToTest() {
		Assertions.assertEquals(orderItemData1.compareTo(orderItemData1), orderItemData1.getItemData().compareTo(orderItemData1.getItemData()));
		Assertions.assertEquals(orderItemData1.compareTo(orderItemData2), orderItemData1.getItemData().compareTo(orderItemData2.getItemData()));
		Assertions.assertEquals(orderItemData1.compareTo(orderItemData3), orderItemData1.getItemData().compareTo(orderItemData3.getItemData()));
		
		Assertions.assertEquals(orderItemData2.compareTo(orderItemData1), orderItemData2.getItemData().compareTo(orderItemData1.getItemData()));
		Assertions.assertEquals(orderItemData2.compareTo(orderItemData2), orderItemData2.getItemData().compareTo(orderItemData2.getItemData()));
		Assertions.assertEquals(orderItemData2.compareTo(orderItemData3), orderItemData2.getItemData().compareTo(orderItemData3.getItemData()));
		
		Assertions.assertEquals(orderItemData3.compareTo(orderItemData1), orderItemData3.getItemData().compareTo(orderItemData1.getItemData()));
		Assertions.assertEquals(orderItemData3.compareTo(orderItemData2), orderItemData3.getItemData().compareTo(orderItemData2.getItemData()));
		Assertions.assertEquals(orderItemData3.compareTo(orderItemData3), orderItemData3.getItemData().compareTo(orderItemData3.getItemData()));
	}

	@Test
	void portionsInOrderTest() {
		IDishMenuItemData oi1 = orderItemData1.getItemData();
		Assertions.assertEquals(orderItemData1.getPortionsInOrder().compareTo(oi1.getPortionSize().multiply(orderItemData1.getAmount())), 0);
		
		IDishMenuItemData oi2 = orderItemData2.getItemData();
		Assertions.assertEquals(orderItemData2.getPortionsInOrder().compareTo(oi2.getPortionSize().multiply(orderItemData2.getAmount())), 0);
		
		IDishMenuItemData oi3 = orderItemData3.getItemData();
		Assertions.assertEquals(orderItemData3.getPortionsInOrder().compareTo(oi3.getPortionSize().multiply(orderItemData3.getAmount())), 0);
	}
	
	@Test
	void grossPricePerPortionTest() {
		IDishMenuItemData oi1 = orderItemData1.getItemData();
		Assertions.assertEquals(orderItemData1.getGrossPricePerPortion().compareTo(oi1.getGrossPricePerPortion()), 0);
		
		IDishMenuItemData oi2 = orderItemData2.getItemData();
		Assertions.assertEquals(orderItemData2.getGrossPricePerPortion().compareTo(oi2.getGrossPricePerPortion()), 0);
		
		IDishMenuItemData oi3 = orderItemData3.getItemData();
		Assertions.assertEquals(orderItemData3.getGrossPricePerPortion().compareTo(oi3.getGrossPricePerPortion()), 0);
	}
	
	@Test
	void grossPricePerMenuItemTest() {
		IDishMenuItemData oi1 = orderItemData1.getItemData();
		Assertions.assertEquals(orderItemData1.getGrossPricePerMenuItem().compareTo(oi1.getGrossPrice()), 0);
		
		IDishMenuItemData oi2 = orderItemData2.getItemData();
		Assertions.assertEquals(orderItemData2.getGrossPricePerMenuItem().compareTo(oi2.getGrossPrice()), 0);
		
		IDishMenuItemData oi3 = orderItemData3.getItemData();
		Assertions.assertEquals(orderItemData3.getGrossPricePerMenuItem().compareTo(oi3.getGrossPrice()), 0);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equalityTest() {
		Assertions.assertTrue(orderItemData1.equals(orderItemData1));
		Assertions.assertTrue(orderItemData2.equals(orderItemData2));
		Assertions.assertTrue(orderItemData3.equals(orderItemData3));
		
		Assertions.assertFalse(orderItemData1.equals(orderItemData2));
		Assertions.assertFalse(orderItemData1.equals(orderItemData3));
		
		Assertions.assertFalse(orderItemData2.equals(orderItemData1));
		Assertions.assertFalse(orderItemData2.equals(orderItemData3));
		
		Assertions.assertFalse(orderItemData3.equals(orderItemData2));
		Assertions.assertFalse(orderItemData3.equals(orderItemData1));
		
		Assertions.assertTrue(orderItemData1.equals(fac.constructData(orderItemData1.getItemData(), orderItemData1.getAmount())));
		Assertions.assertTrue(orderItemData2.equals(fac.constructData(orderItemData2.getItemData(), orderItemData2.getAmount())));
		Assertions.assertTrue(orderItemData3.equals(fac.constructData(orderItemData3.getItemData(), orderItemData3.getAmount())));
		
		Assertions.assertFalse(orderItemData1.equals(null));
		Assertions.assertFalse(orderItemData1.equals(orderItem1));
	}
	
	@Test
	void combineTest() {
		AccumulatingOrderItemAggregate sameCombinedOrderItems = orderItemData1.combine(orderItemData1);
		Assertions.assertTrue(sameCombinedOrderItems.getItemData().equals(orderItemData1.getItemData()));
		Assertions.assertEquals(sameCombinedOrderItems.getAmount().compareTo(orderItemData1.getAmount().add(orderItemData1.getAmount())), 0);
		
		AccumulatingOrderItemAggregate differentCombinedOrderItems = orderItemData1.combine(orderItemData2);
		Assertions.assertTrue(differentCombinedOrderItems.getItemData().equals(orderItemData1.getItemData()));
		Assertions.assertEquals(differentCombinedOrderItems.getAmount().compareTo(orderItemData1.getAmount().add(orderItemData2.getAmount())), 0);
		
		Assertions.assertThrows(IllegalArgumentException.class, ()->{orderItemData1.combine(orderItemData3);});
	}
}
