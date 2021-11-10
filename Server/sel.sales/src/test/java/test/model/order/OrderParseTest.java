package test.model.order;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.order.IOrderData;
import server.model.IServerModel;
import server.model.ServerModel;
@Execution(value = ExecutionMode.SAME_THREAD)
class OrderParseTest {
	private static IServerModel model;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private String discName = "disc";
	private BigDecimal discPorSize = BigDecimal.ONE;
	private BigDecimal discPrice = BigDecimal.valueOf(-1);
	private BigDecimal discProCost = BigDecimal.ONE;
	private String discID = "discID";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void startUp() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(discName, discID, discPorSize, discProCost, discPrice));
	}
	
	@Test
	void orderSpecificDataTest() {
		model.addUnconfirmedOrder("order1#20200809112233000#0#0:item1,2;");
		model.addUnconfirmedOrder("order2#20200809235959532#1#0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3#20200809000000999#1#1:item3,5;");
		
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
	void orderContentTest() {
		model.addUnconfirmedOrder("order1#20200809112233000#0#0:item1,2;");
		model.addUnconfirmedOrder("order2#20200809235959532#1#0:item1,2;item2,3;");
		model.addUnconfirmedOrder("order3#20200809000000999#1#1:item3,5;");
		
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], new BigDecimal[] {BigDecimal.valueOf(2)}, new String[] {"item1"});
		OrderTestUtilityClass.assertOrderDataEqual(orderData[1], new BigDecimal[] {BigDecimal.valueOf(2), BigDecimal.valueOf(3)}, new String[] {"item1", "item2"});
		OrderTestUtilityClass.assertOrderDataEqual(orderData[2], new BigDecimal[] {BigDecimal.valueOf(5)}, new String[] {"item3"});
	}
	
	@Test
	void discountedOrderTest() {
		model.addUnconfirmedOrder("order4#20200809112233000#0#1#12:item1,2;discID,12;");
		
		IOrderData[] orderData = model.getAllUnconfirmedOrders();
		
		LocalDateTime date1 = LocalDateTime.of(2020, 8, 9, 11, 22, 33);
		date1.plusNanos(0);
		
		OrderTestUtilityClass.assertOrderDataEqual(orderData[0], "order4", date1, false, true, BigDecimal.valueOf(12));
	}
}
