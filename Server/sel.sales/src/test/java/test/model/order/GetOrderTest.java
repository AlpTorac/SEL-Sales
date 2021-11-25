package test.model.order;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.order.IOrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class GetOrderTest {
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
	
	private BigDecimal o1a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a2 = BigDecimal.valueOf(3);
	private BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	private String o1id = "order1";
	private String o2id = "order2";
	private String o3id = "order3";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void startUp() {
		model = new ServerModel(this.testFolderAddress);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		model.addUnconfirmedOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.addUnconfirmedOrder(o2id+"#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		model.addUnconfirmedOrder(o3id+"#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
	}
	
	@Test
	void getUnconfirmedOrderTest() {
		IOrderData[] data = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(data.length, 3);
		IOrderData[] gottenOrders = {model.getOrder(o1id), model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(data, gottenOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(data[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(data[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(data[2]));
	}
	@Test
	void getConfirmedOrderTest() {
		model.confirmAllOrders();
		IOrderData[] data = model.getAllConfirmedOrders();
		Assertions.assertEquals(data.length, 3);
		IOrderData[] gottenOrders = {model.getOrder(o1id), model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(data, gottenOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(data[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(data[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(data[2]));
	}
	@Test
	void getMixedOrderTest1() {
		model.confirmOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		IOrderData[] dataC = model.getAllConfirmedOrders();
		IOrderData[] dataU = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(dataC.length, 1);
		Assertions.assertEquals(dataU.length, 2);
		IOrderData[] gottenCOrders = new IOrderData[] {model.getOrder(o1id)};
		IOrderData[] gottenUOrders = new IOrderData[] {model.getOrder(o2id), model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataC, gottenCOrders));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataU, gottenUOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(dataC[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(dataU[0]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(dataU[1]));
	}
	@Test
	void getMixedOrderTest2() {
		model.confirmOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
		model.confirmOrder(o2id+"#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
		IOrderData[] dataC = model.getAllConfirmedOrders();
		IOrderData[] dataU = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(dataC.length, 2);
		Assertions.assertEquals(dataU.length, 1);
		IOrderData[] gottenCOrders = new IOrderData[] {model.getOrder(o1id), model.getOrder(o2id)};
		IOrderData[] gottenUOrders = new IOrderData[] {model.getOrder(o3id)};
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataC, gottenCOrders));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dataU, gottenUOrders));
//		Assertions.assertTrue(model.getOrder(o1id).equals(dataC[0]));
//		Assertions.assertTrue(model.getOrder(o2id).equals(dataC[1]));
//		Assertions.assertTrue(model.getOrder(o3id).equals(dataU[0]));
	}
	@Test
	void getNonExistentOrder() {
		Assertions.assertNull(model.getOrder("gfhidfklhfdhkgfsg"));
	}
}
