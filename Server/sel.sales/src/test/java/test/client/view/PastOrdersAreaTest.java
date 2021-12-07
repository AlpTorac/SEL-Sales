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
class PastOrdersAreaTest extends FXTestTemplate {
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
			
			clientModel.makePendingSendOrder(oData1);
			clientModel.makePendingSendOrder(oData2);
			clientModel.makePendingSendOrder(oData3);
			
			clientModel.makeSentOrder(o2id);
			
			clientOpHelper = new StandardClientViewOperationsUtilityClass(
					clientView, clientController, clientModel);
		});
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
	}
	
	@Test
	void displayedOrdersTest() {
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 2);
		while (clientOpHelper.getPendingSendOrders().toArray(OrderData[]::new).length < clientModel.getAllPendingSendOrders().length) {
			clientView.refreshOrders();
		}
		Assertions.assertEquals(clientOpHelper.getPendingSendOrders().toArray(OrderData[]::new).length, 2);
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getPendingSendOrders().toArray(OrderData[]::new),
				clientModel.getAllPendingSendOrders());
		
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 1);
		while (clientOpHelper.getSentOrders().toArray(OrderData[]::new).length < clientModel.getAllSentOrders().length) {
			clientView.refreshOrders();
		}
		Assertions.assertEquals(clientOpHelper.getSentOrders().toArray(OrderData[]::new).length, 1);
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getSentOrders().toArray(OrderData[]::new),
				clientModel.getAllSentOrders());
	}
}