package test.client;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import client.model.ClientModel;
import client.model.IClientModel;
import model.order.OrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class ClientModelTest {

	private IClientModel clientModel;
	private IServerModel serverModel;
	
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
	void prep() {
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		clientModel = new ClientModel(this.testFolderAddress);
		serverModel = new ServerModel(this.testFolderAddress);
		
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		clientModel.setDishMenu(serverModel.getMenuData());
		
//		clientModel.addCookingOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
//		clientModel.addCookingOrder(o2id+"#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
//		clientModel.addCookingOrder(o3id+"#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
	}

	@AfterEach
	void cleanUp() {
		clientModel.close();
		serverModel.close();
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
	}

	@Test
	void orderCycleTest() {
		String serialisedOrderData = o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";";
		OrderData data = this.clientModel.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		Assertions.assertNull(this.clientModel.getOrder(o1id));
		clientModel.addOrder(serialisedOrderData);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingPaymentOrder(o1id);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingSendOrder(serialisedOrderData);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.orderSent(o1id);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void orderCycleDifferentDataTest() {
		String serialisedOrderData = o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";";
		OrderData data = this.clientModel.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		Assertions.assertNull(this.clientModel.getOrder(o1id));
		clientModel.addOrder(serialisedOrderData);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingPaymentOrder(o1id);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(data));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		String newSerialisedOrderData = o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";";
		OrderData newData = this.clientModel.getOrderHelper().deserialiseOrderData(newSerialisedOrderData);
		
		clientModel.makePendingSendOrder(newSerialisedOrderData);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(newData));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.orderSent(o1id);
		Assertions.assertTrue(this.clientModel.getOrder(o1id).equals(newData));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void getOrderInappropriateParameterTest() {
		Assertions.assertNull(this.clientModel.getOrder("fgdhifakjwafhkbdsf"));
		Assertions.assertNull(this.clientModel.getOrder(null));
	}

	@Test
	void noDuplicateWrittenOrderTest() {
		String serialisedOrderData = o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";";
		
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.addOrder(serialisedOrderData);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makePendingPaymentOrder(o1id);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makeSentOrder(o1id);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.addOrder(serialisedOrderData);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makePendingPaymentOrder(o1id);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		this.clientModel.makeSentOrder(o1id);
		this.clientModel.makePendingSendOrder(serialisedOrderData);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void editOrderTest() {
		String serialisedOrderData = o1id+"#20200809112233343#0#0:item1,5;item2,3";
		OrderData data = this.clientModel.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		
		String newSerialisedOrderData = o2id+"#20200809112233343#0#0:item1,2;item3,6;item2,2;";
		OrderData newData = this.clientModel.getOrderHelper().deserialiseOrderData(newSerialisedOrderData);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		this.clientModel.addOrder(serialisedOrderData);
		this.clientModel.editOrder(data.getID().toString());
		
		Assertions.assertTrue(data.equals(this.clientModel.getEditTarget()));
		
		this.clientModel.addOrder(newSerialisedOrderData);
		this.clientModel.removeOrder(newData.getID().toString());
	
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		this.clientModel.addOrder(serialisedOrderData);
		this.clientModel.makePendingPaymentOrder(data.getID().toString());
		this.clientModel.editOrder(data.getID().toString());
		this.clientModel.addOrder(newSerialisedOrderData);
		this.clientModel.removeOrder(newData.getID().toString());
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void removeAllOrdersTest() {
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		String id4 = "id4";
		
		String so1 = id1+"#20200809112233343#0#0:item1,1;";
		String so2 = id2+"#20200809112233343#0#0:item1,1;";
		String so3 = id3+"#20200809112233343#0#0:item1,1;";
		String so4 = id4+"#20200809112233343#0#0:item1,1;";
		
		clientModel.addCookingOrder(so1);
		clientModel.addCookingOrder(so2);
		clientModel.addCookingOrder(so3);
		clientModel.addCookingOrder(so4);
		
		clientModel.makePendingPaymentOrder(id2);
		
		clientModel.makePendingPaymentOrder(id3);
		clientModel.makePendingSendOrder(so3);
		
		clientModel.makePendingPaymentOrder(id4);
		clientModel.makePendingSendOrder(so4);
		clientModel.makeSentOrder(id4);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 4);
		
		clientModel.removeAllOrders();
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
	}
}
