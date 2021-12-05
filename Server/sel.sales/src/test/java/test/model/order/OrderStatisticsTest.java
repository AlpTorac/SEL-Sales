package test.model.order;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderStatisticsTest extends FXTestTemplate {
	private static IServerModel model;
	
	@BeforeEach
	void startUp() {
		model = this.initServerModel();
		this.initDiscOrders(model);
		this.addOrdersToModel(model);
	}
	
	@Test
	void orderDiscountTest() {
//		OrderData[] orderData = model.getAllUnconfirmedOrders();
		OrderData[] orderData = new OrderData[] {oData1, oData2, oData3};
		
		Assertions.assertEquals(BigDecimal.valueOf(0).compareTo(orderData[0].getOrderDiscount()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(0.3).compareTo(orderData[1].getOrderDiscount()), 0);
//		orderData[2].setOrderDiscount(BigDecimal.valueOf(2));
		Assertions.assertEquals(BigDecimal.valueOf(5).compareTo(orderData[2].getOrderDiscount()), 0);
	}

	@Test
	void grossSumTest() {
//		OrderData[] orderData = model.getAllUnconfirmedOrders();
		OrderData[] orderData = new OrderData[] {oData1, oData2, oData3};
		
		Assertions.assertEquals(BigDecimal.valueOf(10).compareTo(orderData[0].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(13).compareTo(orderData[1].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(20).compareTo(orderData[2].getGrossSum()), 0);
	}
	
	@Test
	void netSumTest() {
//		OrderData[] orderData = model.getAllUnconfirmedOrders();
		OrderData[] orderData = new OrderData[] {oData1, oData2, oData3};
		
//		orderData[0].setOrderDiscount(BigDecimal.valueOf(2));
		Assertions.assertEquals(BigDecimal.valueOf(10).compareTo(orderData[0].getNetSum()), 0);
//		orderData[1].setOrderDiscount(BigDecimal.valueOf(1));
		Assertions.assertEquals(BigDecimal.valueOf(12.7).compareTo(orderData[1].getNetSum()), 0);
//		orderData[2].setOrderDiscount(BigDecimal.valueOf(3));
		Assertions.assertEquals(BigDecimal.valueOf(15).compareTo(orderData[2].getNetSum()), 0);
	}
	
}
