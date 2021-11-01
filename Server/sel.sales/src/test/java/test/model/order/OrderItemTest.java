package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemData;
import model.order.IOrderItem;
import model.order.OrderItem;

class OrderItemTest {

	private static IModel model;
	
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
	private BigDecimal orderItem2a = BigDecimal.valueOf(2);
	private BigDecimal orderItem3a = BigDecimal.valueOf(5);
	
	private IOrderItem orderItem1;
	private IOrderItem orderItem2;
	private IOrderItem orderItem3;
	
	@BeforeEach
	void prep() {
		model = new Model();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		item1 = model.getMenuItem(i1id);
		item2 = model.getMenuItem(i2id);
		item3 = model.getMenuItem(i3id);
		
		orderItem1 = new OrderItem(item1, orderItem1a);
		orderItem2 = new OrderItem(item1, orderItem2a);
		orderItem3 = new OrderItem(item2, orderItem3a);
	}

	@AfterEach
	void cleanUp() {
		model.close();
	}

	@Test
	void compareToTest() {
		Assertions.assertEquals(orderItem1.compareTo(orderItem1), orderItem1.getMenuItemData().compareTo(orderItem1.getMenuItemData()));
		Assertions.assertEquals(orderItem1.compareTo(orderItem2), orderItem1.getMenuItemData().compareTo(orderItem2.getMenuItemData()));
		Assertions.assertEquals(orderItem1.compareTo(orderItem3), orderItem1.getMenuItemData().compareTo(orderItem3.getMenuItemData()));
		
		Assertions.assertEquals(orderItem2.compareTo(orderItem1), orderItem2.getMenuItemData().compareTo(orderItem1.getMenuItemData()));
		Assertions.assertEquals(orderItem2.compareTo(orderItem2), orderItem2.getMenuItemData().compareTo(orderItem2.getMenuItemData()));
		Assertions.assertEquals(orderItem2.compareTo(orderItem3), orderItem2.getMenuItemData().compareTo(orderItem3.getMenuItemData()));
		
		Assertions.assertEquals(orderItem3.compareTo(orderItem1), orderItem3.getMenuItemData().compareTo(orderItem1.getMenuItemData()));
		Assertions.assertEquals(orderItem3.compareTo(orderItem2), orderItem3.getMenuItemData().compareTo(orderItem2.getMenuItemData()));
		Assertions.assertEquals(orderItem3.compareTo(orderItem3), orderItem3.getMenuItemData().compareTo(orderItem3.getMenuItemData()));
	}

	@Test
	void totalPortionsTest() {
		IDishMenuItemData oi1 = orderItem1.getMenuItemData();
		Assertions.assertEquals(orderItem1.getTotalPortions().compareTo(oi1.getPortionSize().multiply(orderItem1.getAmount())), 0);
		
		IDishMenuItemData oi2 = orderItem2.getMenuItemData();
		Assertions.assertEquals(orderItem2.getTotalPortions().compareTo(oi2.getPortionSize().multiply(orderItem2.getAmount())), 0);
		
		IDishMenuItemData oi3 = orderItem3.getMenuItemData();
		Assertions.assertEquals(orderItem3.getTotalPortions().compareTo(oi3.getPortionSize().multiply(orderItem3.getAmount())), 0);
	}
	
	@Test
	void orderItemPriceTest() {
		IDishMenuItemData oi1 = orderItem1.getMenuItemData();
		Assertions.assertEquals(orderItem1.getOrderItemPrice().compareTo(oi1.getGrossPrice().multiply(orderItem1.getAmount())), 0);
		
		IDishMenuItemData oi2 = orderItem2.getMenuItemData();
		Assertions.assertEquals(orderItem2.getOrderItemPrice().compareTo(oi2.getGrossPrice().multiply(orderItem2.getAmount())), 0);
		
		IDishMenuItemData oi3 = orderItem3.getMenuItemData();
		Assertions.assertEquals(orderItem3.getOrderItemPrice().compareTo(oi3.getGrossPrice().multiply(orderItem3.getAmount())), 0);
	}
	
	@Test
	void amountTest() {
		double newAmountDouble = 99;
		BigDecimal newAmount = BigDecimal.valueOf(newAmountDouble);
		
		Assertions.assertEquals(orderItem1.getAmount().compareTo(orderItem1a), 0);
		orderItem1.setAmount(newAmount);
		Assertions.assertEquals(orderItem1.getAmount().compareTo(newAmount), 0);
		
		IDishMenuItemData oi1 = orderItem1.getMenuItemData();
		Assertions.assertEquals(orderItem1.getTotalPortions().compareTo(oi1.getPortionSize().multiply(newAmount)), 0);
		Assertions.assertEquals(orderItem1.getOrderItemPrice().compareTo(oi1.getGrossPrice().multiply(newAmount)), 0);
	}
}
