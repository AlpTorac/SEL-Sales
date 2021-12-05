package test.client.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.model.ClientModel;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.StandardClientView;
import javafx.application.Platform;
import model.dish.DishMenuItemData;
import model.order.OrderData;
import model.order.AccumulatingOrderItemAggregate;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
import test.external.dummy.DummyClientExternal;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class CookingOrdersAreaTest extends FXTestTemplate {
	private IServerModel serverModel;
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
	private DummyClientExternal clientExternal;
	
	private OrderData data;
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			clientModel = this.initClientModel();
			clientController = this.initClientController(clientModel);
			clientView = this.initClientView(clientModel, clientController);
			clientExternal = this.initClientExternal(serviceID, serviceName, clientModel, clientController);
			
			serverModel = this.initServerModel();
			this.addDishMenuToServerModel(serverModel);
			this.getServerMenuIntoClient(serverModel, clientModel, clientView);
			this.initOrders(clientModel);
			this.addOrdersToModel(clientModel);
			this.setClientOpHelper(clientModel, clientController, clientView);
//			clientModel.addCookingOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
//			clientModel.addCookingOrder("order6#20200813000000183#1#1:item3,5;item3,4;");
//			clientModel.addCookingOrder("order7#20200909112233937#0#0:item1,2;item2,5;");
		});
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(clientExternal, clientModel, serverModel);
	}
	
	@Test
	void displayedOrdersTest() {
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 3);
		while (clientOpHelper.getCookingOrders().toArray(OrderData[]::new).length < clientModel.getAllCookingOrders().length) {
			clientView.refreshOrders();
		}
		Assertions.assertEquals(clientOpHelper.getCookingOrders().toArray(OrderData[]::new).length, 3);
		
		GeneralTestUtilityClass.arrayContentEquals(clientOpHelper.getCookingOrders().toArray(OrderData[]::new),
				clientModel.getAllCookingOrders());
	}
}