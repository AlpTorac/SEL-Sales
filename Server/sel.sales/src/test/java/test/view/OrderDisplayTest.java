package test.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.order.OrderData;
import server.controller.IServerController;
import server.model.IServerModel;
import server.view.StandardServerView;
import test.FXTestTemplate;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderDisplayTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	
	@BeforeEach
	void prep() {
		runFXAction(() -> {
			model = this.initServerModel();
			controller = this.initServerController(model);
			view = this.initServerView(model, controller);
			view.startUp();
			view.show();
			this.addDishMenuToServerModel(model);
			this.initOrders(model);
			this.addOrdersToModel(model);
			this.setServerOpHelper(model, controller, view);
		});
	}
	
	@AfterEach
	void cleanUp() {
		runFXAction(() -> {
			view.hide();
			this.closeModel(model);
		});
	}
	
	@Test
	void unconfirmedOrderShowingTest() {
		runFXAction(() -> {
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
//			opHelper.clickOnUnconfirmedOrder(0);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[0]);
//			opHelper.clickOnUnconfirmedOrder(1);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[1]);
//			opHelper.clickOnUnconfirmedOrder(2);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[2]);
		});
	}
	
	@Test
	void multiClickOrderShowingTest() {
		runFXAction(() -> {
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			serverOpHelper.clickOnUnconfirmedOrder(0);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[0]);
			serverOpHelper.clickOnUnconfirmedOrder(1);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[1]);
			serverOpHelper.clickOnUnconfirmedOrder(2);
			serverOpHelper.assertShownOrderEquals(unconfirmedOrders[2]);
		});
		runFXAction(() -> {
			model.confirmAllOrders();
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			serverOpHelper.clickOnConfirmedOrder(0);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[0]);
			serverOpHelper.clickOnConfirmedOrder(1);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[1]);
			serverOpHelper.clickOnConfirmedOrder(2);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[2]);
		});
	}
	
	@Test
	void confirmedOrderShowingTest() {
		runFXAction(() -> {
			serverOpHelper.confirmAllOrdersWithoutReturn();
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
//			opHelper.clickOnConfirmedOrder(0);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[0]);
//			opHelper.clickOnConfirmedOrder(1);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[1]);
//			opHelper.clickOnConfirmedOrder(2);
			serverOpHelper.assertShownOrderEquals(confirmedOrders[2]);
		});
	}
}
