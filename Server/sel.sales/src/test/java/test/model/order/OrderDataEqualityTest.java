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

class OrderDataEqualityTest {
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
		
		model.addUnconfirmedOrder("order1-20200809112233-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200809235959-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20200809000000-1-1:item3,5;");
	}
	
	@Test
	void notEqualTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		Assertions.assertFalse(orderData[0].equals(orderData[1]));
		Assertions.assertFalse(orderData[0].equals(orderData[2]));
		Assertions.assertFalse(orderData[1].equals(orderData[0]));
		Assertions.assertFalse(orderData[1].equals(orderData[2]));
		Assertions.assertFalse(orderData[2].equals(orderData[0]));
		Assertions.assertFalse(orderData[2].equals(orderData[1]));
	}
	
	@Test
	void equalTest() {
		IOrderData[] orderData1 = model.getAllUnconfirmedOrders();
		IOrderData[] orderData2 = model.getAllUnconfirmedOrders();
		
		// Make sure references are different
		Assertions.assertFalse(orderData1[0] == orderData2[0]);
		Assertions.assertFalse(orderData1[1] == orderData2[1]);
		Assertions.assertFalse(orderData1[2] == orderData2[2]);
		
		Assertions.assertTrue(orderData1[0].equals(orderData2[0]));
		Assertions.assertTrue(orderData1[1].equals(orderData2[1]));
		Assertions.assertTrue(orderData1[2].equals(orderData2[2]));
	}
}
