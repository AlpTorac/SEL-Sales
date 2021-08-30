package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;

class OrderAccumulationTest {

	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	@AfterEach
	void afterTest() {
		model.removeAllOrders();
	}
	
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
		
		model.addOrder("order2-20200809235959-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
	}

	@Test
	void test() {
		IOrderData[] orderData = model.getAllOrders();
		
		OrderTestUtilityClass.assertOrderDataEqual(
				orderData[0],
				new BigDecimal[] {BigDecimal.valueOf(9), BigDecimal.valueOf(3), BigDecimal.valueOf(6)},
				new String[] {"item1", "item2", "item3"});
	}
}
