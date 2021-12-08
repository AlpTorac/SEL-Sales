package test.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.model.IClientModel;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class ClientModelTest extends FXTestTemplate {

	private IClientModel clientModel;
	private IServerModel serverModel;
	
	@BeforeEach
	void prep() {
		this.cleanTestFolder();
		clientModel = this.initClientModel();
		serverModel = this.initServerModel();
		
		this.addDishMenuToServerModel(serverModel);
		this.getServerMenuIntoClient(serverModel, clientModel);
		this.initOrders(clientModel);
		
//		clientModel.addCookingOrder(o1id+"#20200809112233343#0#0:item1,"+o1a1.toPlainString()+";");
//		clientModel.addCookingOrder(o2id+"#20200809235959111#1#0:item1,"+o2a1.toPlainString()+";item2,"+o2a2.toPlainString()+";");
//		clientModel.addCookingOrder(o3id+"#20200809000000222#1#1:item3,"+o3a3.toPlainString()+";");
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
	}

	@Test
	void orderCycleTest() {
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		Assertions.assertNull(this.clientModel.getOrder(o1id));
		clientModel.addOrder(oData1);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingPaymentOrder(o1id);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingSendOrder(oData1);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.orderSentByID(o1id);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void orderCycleDifferentDataTest() {
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		Assertions.assertNull(this.clientModel.getOrder(o1id));
		clientModel.addOrder(oData1);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.makePendingPaymentOrder(o1id);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), oData1));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		OrderData newData = clientModel.getOrder(o1id);
		newData.addOrderItem(iData2, o1a1);
		
		clientModel.makePendingSendOrder(newData);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), newData));
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		clientModel.orderSentByID(o1id);
		Assertions.assertTrue(this.ordersEqual(this.clientModel.getOrder(o1id), newData));
		
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
		OrderData data = oData1;
		
		this.clientModel.addOrder(data);
		this.clientModel.addCookingOrder(data);
		this.clientModel.makePendingPaymentOrder(data.getID().toString());
		
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.addOrder(data);
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makePendingPaymentOrder(data.getID().toString());
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makeSentOrder(data.getID().toString());
		this.clientModel.makePendingSendOrder(data);
		
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.addOrder(data);
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makePendingPaymentOrder(data.getID().toString());
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makePendingSendOrder(data);
		this.clientModel.makeSentOrder(data.getID().toString());
		this.clientModel.makePendingSendOrder(data);
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 1);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void editOrderTest() {
		OrderData data = oData1;
		OrderData newData = oData2;
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 0);
		
		this.clientModel.addOrder(data);
		this.clientModel.editOrder(data.getID().toString());
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(data, this.clientModel.getEditTarget()));
		
		this.clientModel.addOrder(newData);
		this.clientModel.removeOrder(newData.getID().toString());
	
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
		
		this.clientModel.addOrder(data);
		this.clientModel.makePendingPaymentOrder(data.getID().toString());
		this.clientModel.editOrder(data.getID().toString());
		this.clientModel.addOrder(newData);
		this.clientModel.removeOrder(newData.getID().toString());
		
		Assertions.assertEquals(this.clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(this.clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void removeAllOrdersTest() {
		OrderData data = this.clientModel.getOrderFactory().constructData("order4", clientModel.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809112233343",clientModel)), false, false);
		data.addOrderItem(iData1, o1a1);
		
		clientModel.addCookingOrder(oData1);
		clientModel.addCookingOrder(oData2);
		clientModel.addCookingOrder(oData3);
		clientModel.addCookingOrder(data);
		
		clientModel.makePendingPaymentOrder(oData2.getID().toString());
		
		clientModel.makePendingPaymentOrder(oData3.getID().toString());
		clientModel.makePendingSendOrder(oData3);
		
		clientModel.makePendingPaymentOrder(data.getID().toString());
		clientModel.makePendingSendOrder(data);
		clientModel.makeSentOrder(data.getID().toString());
		
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
