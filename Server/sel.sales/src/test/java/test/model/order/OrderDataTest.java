package test.model.order;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import model.order.AccumulatingOrderItemAggregate;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.TestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderDataTest extends TestTemplate {
	private IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.addDishMenuToServerModel(model);
		this.addOrdersToServerModel(model);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	@Test
	void notEqualTest() {
		OrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertFalse(orderData[0].equals(orderData[1]));
		Assertions.assertFalse(orderData[0].equals(orderData[2]));
		Assertions.assertFalse(orderData[1].equals(orderData[0]));
		Assertions.assertFalse(orderData[1].equals(orderData[2]));
		Assertions.assertFalse(orderData[2].equals(orderData[0]));
		Assertions.assertFalse(orderData[2].equals(orderData[1]));
	}
	
	@Test
	void equalTest() {
		OrderData[] orderData1 = model.getAllUnconfirmedOrders();
		OrderData[] orderData2 = model.getAllUnconfirmedOrders();
		
		// Make sure references are different
		Assertions.assertFalse(orderData1[0] == orderData2[0]);
		Assertions.assertFalse(orderData1[1] == orderData2[1]);
		Assertions.assertFalse(orderData1[2] == orderData2[2]);
		
		for (OrderData od2 : orderData2) {
			GeneralTestUtilityClass.arrayContains(orderData1, od2);
		}
		
//		Assertions.assertTrue(orderData1[0].equals(orderData2[0]));
//		Assertions.assertTrue(orderData1[1].equals(orderData2[1]));
//		Assertions.assertTrue(orderData1[2].equals(orderData2[2]));
	}
	
	@Test
	void contentTest() {
		Assertions.assertTrue(oData1.getOrderedItemAmount(iData1.getID()).compareTo(o1a1) == 0);
		Assertions.assertTrue(oData3.getOrderedItemAmount(iData3.getID()).compareTo(o3a3) == 0);
		Assertions.assertTrue(oData2.getOrderedItemAmount(iData1.getID()).compareTo(o2a1) == 0);
		Assertions.assertTrue(oData2.getOrderedItemAmount(iData2.getID()).compareTo(o2a2) == 0);
		
		Assertions.assertTrue(model.getOrder(o1id).getOrderedItemAmount(iData1.getID()).compareTo(o1a1) == 0);
		Assertions.assertTrue(model.getOrder(o3id).getOrderedItemAmount(iData3.getID()).compareTo(o3a3) == 0);
		Assertions.assertTrue(model.getOrder(o2id).getOrderedItemAmount(iData1.getID()).compareTo(o2a1) == 0);
		Assertions.assertTrue(model.getOrder(o2id).getOrderedItemAmount(iData2.getID()).compareTo(o2a2) == 0);
	}
	
	@Test
	void combineTest() {
		OrderData[] preModelOrders = new OrderData[] {oData1, oData2, oData3};
		
		OrderData combinedOrder = preModelOrders[0];
		
		for (int i = 1; i < preModelOrders.length; i++) {
			combinedOrder.addAllOrderItems(preModelOrders[i].getOrderedItems());
		}
		
		Assertions.assertTrue(combinedOrder.getOrderedItemAmount(iData2.getID()).compareTo(o2a2) == 0);
		Assertions.assertTrue(combinedOrder.getOrderedItemAmount(iData3.getID()).compareTo(o3a3) == 0);
		Assertions.assertEquals(combinedOrder.getOrderedItemAmount(iData1.getID()).doubleValue(), o1a1.add(o2a1).doubleValue(), 1E-3);
		Assertions.assertTrue(combinedOrder.getOrderedItemAmount(iData1.getID()).compareTo(o1a1.add(o2a1)) == 0);
		
		OrderData[] orders = model.getAllUnconfirmedOrders();
		
		OrderData o1 = orders[0];
		
		for (int i = 1; i < orders.length; i++) {
			o1.addAllOrderItems(orders[i].getOrderedItems());
		}
		
		Assertions.assertTrue(o1.getOrderedItemAmount(iData2.getID()).compareTo(o2a2) == 0);
		Assertions.assertTrue(o1.getOrderedItemAmount(iData3.getID()).compareTo(o3a3) == 0);
		Assertions.assertEquals(o1.getOrderedItemAmount(iData1.getID()).doubleValue(), o1a1.add(o2a1).doubleValue(), 1E-3);
		Assertions.assertTrue(o1.getOrderedItemAmount(iData1.getID()).compareTo(o1a1.add(o2a1)) == 0);
	}
}
