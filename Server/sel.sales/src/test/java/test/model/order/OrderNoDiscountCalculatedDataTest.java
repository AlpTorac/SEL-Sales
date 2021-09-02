package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;

class OrderNoDiscountCalculatedDataTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	@BeforeAll
	static void startUp() {
		model = new Model();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
		
		model.addUnconfirmedOrder("order1-20200809112233343-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200809235959111-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20200809000000222-1-1:item3,5;");
	}
	@Test
	void totalDiscountTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(BigDecimal.valueOf(0).compareTo(orderData[0].getTotalDiscount()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(0).compareTo(orderData[1].getTotalDiscount()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(0).compareTo(orderData[2].getTotalDiscount()), 0);
	}

	@Test
	void grossSumTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(BigDecimal.valueOf(10).compareTo(orderData[0].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(13).compareTo(orderData[1].getGrossSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(20).compareTo(orderData[2].getGrossSum()), 0);
	}
	
	@Test
	void netSumTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertEquals(BigDecimal.valueOf(10).compareTo(orderData[0].getNetSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(13).compareTo(orderData[1].getNetSum()), 0);
		Assertions.assertEquals(BigDecimal.valueOf(20).compareTo(orderData[2].getNetSum()), 0);
	}
	
}
