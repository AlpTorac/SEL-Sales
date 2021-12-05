package test.model.order;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderParseTest extends FXTestTemplate {
	private static IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.initDiscOrders(model);
		this.addOrdersToModel(model);
	}
	
	@Test
	void orderSpecificDataTest() {
//		OrderData[] orderData = model.getAllUnconfirmedOrders();
		OrderData[] orderData = new OrderData[] {oData1, oData2, oData3};
		
		LocalDateTime date1 = LocalDateTime.of(2020, 8, 9, 11, 22, 33);
		date1 = date1.plusNanos(343000000);
		LocalDateTime date2 = LocalDateTime.of(2020, 8, 9, 23, 59, 59);
		date2 = date2.plusNanos(111000000);
		LocalDateTime date3 = LocalDateTime.of(2020, 8, 9, 0, 0, 0);
		date3 = date3.plusNanos(222000000);
		
		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], o1id, date1, false, false);
		OrderTestUtilityClass.assertOrderDataEqual(orderData[1], o2id, date2, true, false);
		OrderTestUtilityClass.assertOrderDataEqual(orderData[2], o3id, date3, true, true);
	}
	
	@Test
	void orderContentTest() {
		OrderData[] orderData = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(orderData.length, 3);
		
//		AccumulatingOrderItemAggregate i1d = model.getOrderHelper().createOrderItem(item1, BigDecimal.valueOf(2));
//		AccumulatingOrderItemAggregate i2d = model.getOrderHelper().createOrderItem(item2, BigDecimal.valueOf(3));
//		AccumulatingOrderItemAggregate i3d = model.getOrderHelper().createOrderItem(item3, BigDecimal.valueOf(5));
//		
//		AccumulatingOrderItemAggregate[] o1 = new AccumulatingOrderItemAggregate[] {i1d};
//		AccumulatingOrderItemAggregate[] o2 = new AccumulatingOrderItemAggregate[] {i1d,i2d};
//		AccumulatingOrderItemAggregate[] o3 = new AccumulatingOrderItemAggregate[] {i3d};
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(oData1.getOrderedItems(), orderItem1));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(oData2.getOrderedItems(), orderItem2));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(oData3.getOrderedItems(), orderItem3));
		
//		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], new BigDecimal[] {BigDecimal.valueOf(2)}, new String[] {"item1"});
//		OrderTestUtilityClass.assertOrderDataEqual(orderData[1], new BigDecimal[] {BigDecimal.valueOf(2), BigDecimal.valueOf(3)}, new String[] {"item1", "item2"});
//		OrderTestUtilityClass.assertOrderDataEqual(orderData[2], new BigDecimal[] {BigDecimal.valueOf(5)}, new String[] {"item3"});
	}
	
	@Test
	void discountedOrderTest() {
//		model.addUnconfirmedOrder("order4#20200809112233000#0#1#12:item1,2;discID,12;");
//		
//		OrderData[] orderData = model.getAllUnconfirmedOrders();
		
		LocalDateTime date1 = LocalDateTime.of(2020, 8, 9, 0, 0, 0);
		date1 = date1.plusNanos(222000000);
		
		OrderTestUtilityClass.assertOrderDataEqual(model.getOrder(o3id), o3id, date1, true, true, o3ad);
	}
}
