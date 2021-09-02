package test.model.order;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrder;
import model.order.IOrderData;
import model.order.IOrderItemData;

class OrderParseTest {
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
		
		model.addUnconfirmedOrder("order1-20200809112233000-0-0:item1,2;");
		model.addUnconfirmedOrder("order2-20200809235959532-1-0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3-20200809000000999-1-1:item3,5;");
	}
	
	@Test
	void orderSpecificDataTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		LocalDateTime date1 = LocalDateTime.of(2020, 8, 9, 11, 22, 33);
		date1.plusNanos(0);
		LocalDateTime date2 = LocalDateTime.of(2020, 8, 9, 23, 59, 59);
		date2 = date2.plusNanos(532000000);
		LocalDateTime date3 = LocalDateTime.of(2020, 8, 9, 0, 0, 0);
		date3 = date3.plusNanos(999000000);
		
		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], "order1", date1, false, false);
		OrderTestUtilityClass.assertOrderDataEqual(orderData[1], "order2", date2, true, false);
		OrderTestUtilityClass.assertOrderDataEqual(orderData[2], "order3", date3, true, true);
	}
	
	@Test
	void OrderContentTest() {
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], new BigDecimal[] {BigDecimal.valueOf(2)}, new String[] {"item1"});
		OrderTestUtilityClass.assertOrderDataEqual(orderData[1], new BigDecimal[] {BigDecimal.valueOf(2), BigDecimal.valueOf(3)}, new String[] {"item1", "item2"});
		OrderTestUtilityClass.assertOrderDataEqual(orderData[2], new BigDecimal[] {BigDecimal.valueOf(5)}, new String[] {"item3"});
	}
}
