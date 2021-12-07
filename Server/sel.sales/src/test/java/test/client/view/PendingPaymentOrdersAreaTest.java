package test.client.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.IClientView;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class PendingPaymentOrdersAreaTest extends FXTestTemplate {
	private IServerModel serverModel;
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
	
	private OrderData data;
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			clientModel = this.initClientModel();
			clientController = this.initClientController(clientModel);
			clientView = this.initClientView(clientModel, clientController);
			
			serverModel = this.initServerModel();
			this.initDishMenuItems(serverModel);
			this.addDishMenuToServerModel(serverModel);
			this.initOrders(clientModel);
			
			this.getPrivateFieldsFromModel(clientModel);
			
			this.getServerMenuIntoClient(serverModel, clientModel);
			clientView.refreshMenu();
			
			clientModel.addCookingOrder(oData1);
			clientModel.addCookingOrder(oData2);
			clientModel.addCookingOrder(oData3);
			
			clientModel.makePendingPaymentOrder(o1id);
			clientModel.makePendingPaymentOrder(o2id);
			clientModel.makePendingPaymentOrder(o3id);
			
			clientView.refreshOrders();
			
			this.setClientOpHelper(clientModel, clientController, clientView);
		});
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
	}
	
	@Test
	void displayedOrdersTest() {
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 3);
		
		while (clientOpHelper.getPendingPaymentOrders().toArray(OrderData[]::new).length < clientModel.getAllPendingPaymentOrders().length) {
			clientView.refreshOrders();
		}
		
		Assertions.assertEquals(clientOpHelper.getPendingPaymentOrders().toArray(OrderData[]::new).length, 3);
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getPendingPaymentOrders().toArray(OrderData[]::new),
				clientModel.getAllPendingPaymentOrders());
	}
	
	@Test
	void optionsTest() {
		this.displayedOrdersTest();
		
		boolean o1IsCash = false;
		boolean o1IsHere = true;
		runFXAction(()->{
			clientOpHelper.ppoaSetPaymentOption(o1id, o1IsCash);
			clientOpHelper.ppoaSetPlaceOption(o1id, o1IsHere);
		});
		Assertions.assertTrue(clientOpHelper.ppoaGetIsCash(o1id) == o1IsCash);
		
		
		boolean o2IsCash = false;
		boolean o2IsHere = false;
		runFXAction(()->{
			clientOpHelper.ppoaSetPaymentOption(o2id, o2IsCash);
			clientOpHelper.ppoaSetPlaceOption(o2id, o2IsHere);
		});
		Assertions.assertTrue(clientOpHelper.ppoaGetIsCash(o2id) == o2IsCash);
		Assertions.assertTrue(clientOpHelper.ppoaGetIsHere(o2id) == o2IsHere);
		
		boolean o3IsCash = true;
		boolean o3IsHere = false;
		runFXAction(()->{
			clientOpHelper.ppoaSetPlaceOption(o3id, o3IsHere);
			clientOpHelper.ppoaSetPaymentOption(o3id, o3IsCash);
		});
		Assertions.assertTrue(clientOpHelper.ppoaGetIsHere(o3id) == o3IsHere);
	}
	
	@Test
	void nextTabTest() {
		this.optionsTest();
		
		OrderData d1PPOA = orderDAO.parseValueObject(clientOpHelper.ppoaGetSerialisedOrder(o1id));
		Assertions.assertFalse(d1PPOA.getIsCash());
		Assertions.assertTrue(d1PPOA.getIsHere());
		
		OrderData d2PPOA = orderDAO.parseValueObject(clientOpHelper.ppoaGetSerialisedOrder(o2id));
		Assertions.assertFalse(d2PPOA.getIsCash());
		Assertions.assertFalse(d2PPOA.getIsHere());
		
		OrderData d3PPOA = orderDAO.parseValueObject(clientOpHelper.ppoaGetSerialisedOrder(o3id));
		Assertions.assertTrue(d3PPOA.getIsCash());
		Assertions.assertFalse(d3PPOA.getIsHere());
		
		OrderData[] dPPOA = new OrderData[] {d1PPOA, d2PPOA, d3PPOA};
		OrderData[] dModel = clientModel.getAllPendingPaymentOrders();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dPPOA, dModel,
				(o1,o2)->this.ordersEqual(o1, o2)));
	}
}
