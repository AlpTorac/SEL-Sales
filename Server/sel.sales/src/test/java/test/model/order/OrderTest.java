package test.model.order;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dish.IDishMenuItemData;
import model.id.EntityIDFactory;
import model.id.FixIDFactory;
import model.order.IOrder;
import model.order.IOrderDataFactory;
import model.order.IOrderItemData;
import model.order.IOrderItemDataFactory;
import model.order.Order;
import model.order.OrderDataFactory;
import model.order.OrderItemDataFactory;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;

class OrderTest {
	
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
	
	private String o1id = "order1";
	private String o2id = "order2";
	
	private IOrder order1;
	private IOrder order2;
	
	private BigDecimal o1a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a2 = BigDecimal.valueOf(3);
	private BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	private EntityIDFactory idFac = new FixIDFactory();
	
	private IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
	private IOrderDataFactory orderDataFac = new OrderDataFactory(orderItemDataFac, idFac);
	
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
		
		order1 = new Order(LocalDateTime.now(), false, false, idFac.createID(o1id));
		order2 = new Order(LocalDateTime.now(), false, false, idFac.createID(o2id));
	}

	@AfterEach
	void cleanUp() {
		model.close();
	}

	@Test
	void compareToTest() {
		Assertions.assertEquals(order1.compareTo(order1), o1id.compareTo(o1id));
		Assertions.assertEquals(order2.compareTo(order2), o2id.compareTo(o2id));
		
		Assertions.assertEquals(order1.compareTo(order2), o1id.compareTo(o2id));
		Assertions.assertEquals(order2.compareTo(order1), o2id.compareTo(o1id));
	}

	@Test
	void addOrderItemTest() {
		IOrderItemData addedItem = orderItemDataFac.constructData(item1, o1a1);
		order1.addOrderItem(addedItem);
		
		Collection<IOrderItemData> orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(addedItem));
		
		order1.addOrderItem(addedItem);
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(orderItemDataFac.constructData(item1, o1a1.multiply(BigDecimal.valueOf(2)))));
		
		IOrderItemData addedItem2 = orderItemDataFac.constructData(item2, BigDecimal.valueOf(2));
		
		order1.addOrderItem(addedItem2);
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 2
				&& orderItems.contains(orderItemDataFac.constructData(item1, o1a1.multiply(BigDecimal.valueOf(2))))
				&& orderItems.contains(addedItem2));
	}
	
	@Test
	void setOrderItemAmountTest() {
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderItemData addedItem = orderItemDataFac.constructData(item1, o1a1);
		order1.addOrderItem(addedItem);
		
		Collection<IOrderItemData> orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(addedItem));
		
		BigDecimal newAmount = BigDecimal.valueOf(5);
		Assertions.assertTrue(order1.setOrderedItemAmount(i1id, newAmount));
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(orderItemDataFac.constructData(item1, newAmount)));
	
		Assertions.assertFalse(order1.setOrderedItemAmount(i2id, newAmount));
		Assertions.assertFalse(orderItems.contains(orderItemDataFac.constructData(item2, newAmount)));
	}
	
	@Test
	void removeOrderItemTest() {
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderItemData addedItem = orderItemDataFac.constructData(item1, o1a1);
		order1.addOrderItem(addedItem);
		
		Collection<IOrderItemData> orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(addedItem));
		
		IOrderItemData addedItem2 = orderItemDataFac.constructData(item2, BigDecimal.valueOf(2));
		
		order1.addOrderItem(addedItem2);
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 2
				&& orderItems.contains(addedItem)
				&& orderItems.contains(addedItem2));
		
		Assertions.assertTrue(order1.removeOrderItem(i1id));
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1
				&& orderItems.contains(addedItem2));
		
		Assertions.assertTrue(order1.removeOrderItem(i2id));
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.isEmpty());
	}
	
	@Test
	void getOrderItemTest() {
		IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
		IOrderItemData addedItem = orderItemDataFac.constructData(item1, o1a1);
		order1.addOrderItem(addedItem);
		
		Collection<IOrderItemData> orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 1 && orderItems.contains(addedItem));
		
		BigDecimal item2Amount = BigDecimal.valueOf(2);
		IOrderItemData addedItem2 = orderItemDataFac.constructData(item2, item2Amount);
		
		order1.addOrderItem(addedItem2);
		orderItems = orderDataFac.orderToData(order1).getOrderItems();
		Assertions.assertTrue(orderItems.size() == 2
				&& orderItems.contains(addedItem)
				&& orderItems.contains(addedItem2));
		
		Assertions.assertTrue(order1.getOrderItem(i1id).getAmount().compareTo(o1a1) == 0);
		Assertions.assertTrue(order1.getOrderItem(i2id).getAmount().compareTo(item2Amount) == 0);
		
		
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(orderDataFac.orderToData(order1).getOrderedItems(), new IOrderItemData[] {addedItem, addedItem2}));
	}
}
