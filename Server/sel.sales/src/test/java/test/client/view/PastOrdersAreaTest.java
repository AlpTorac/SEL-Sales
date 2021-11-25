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
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClientExternal;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
@Execution(value = ExecutionMode.SAME_THREAD)
class PastOrdersAreaTest extends ApplicationTest {
	private IDishMenuItemData item1;
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private IDishMenuItemData item2;
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private IDishMenuItemData item3;
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private IServerModel serverModel;
	
	private IClientModel clientModel;
	private IClientController clientController;
	private IClientView clientView;
	private DummyClientExternal clientExternal;
	
	private String serviceID = "serviceID";
	private String serviceName = "serviceName";
	private String deviceName = "deviceName";
	private String deviceAddress = "deviceAddress";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private StandardClientViewOperationsUtilityClass opHelper;
	
	private IOrderData data;
	
	private String o1id = "order2";
	private String o2id = "order6";
	private String o3id = "order7";
	
	private String so1;
	private String so2;
	private String so3;
	
	private volatile boolean actionFinished = false;
	
	private void waitForAction() {
		while (!actionFinished) {
			
		}
		actionFinished = false;
	}
	
	private void runFXAction(Runnable run) {
		Platform.runLater(() -> {
			run.run();
			actionFinished = true;
		});
		waitForAction();
	}
	
	@BeforeEach
	void prep() {
		runFXAction(()->{
			clientModel = new ClientModel(this.testFolderAddress);
			clientController = new StandardClientController(clientModel);
			clientView = new StandardClientView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), clientController, clientModel);
			clientExternal = new DummyClientExternal(serviceID, serviceName, clientController, clientModel);
			
			serverModel = new ServerModel(this.testFolderAddress);
			serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
			serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
			serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
			clientModel.setDishMenu(serverModel.getMenuData());
			clientView.refreshMenu();
			
			item1 = clientModel.getMenuItem(i1id);
			item2 = clientModel.getMenuItem(i2id);
			item3 = clientModel.getMenuItem(i3id);
			
			so1 = o1id+"#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1";
			so2 = o2id+"#20200813000000183#1#1:item3,5;item3,4;";
			so3 = o3id+"#20200909112233937#0#0:item1,2;item2,5;";
			
			clientModel.addCookingOrder(so1);
			clientModel.addCookingOrder(so2);
			clientModel.addCookingOrder(so3);
			
			clientModel.makePendingPaymentOrder(o1id);
			clientModel.makePendingPaymentOrder(o2id);
			clientModel.makePendingPaymentOrder(o3id);
			
			clientModel.makePendingSendOrder(o1id, so1);
			clientModel.makePendingSendOrder(o2id, so2);
			clientModel.makePendingSendOrder(o3id, so3);
			
			clientModel.makeSentOrder(o2id);
			
			opHelper = new StandardClientViewOperationsUtilityClass(
					clientView, clientController, clientModel);
		});
	}

	@AfterEach
	void cleanUp() {
		clientExternal.close();
		clientModel.close();
		serverModel.close();
	}
	
	@Test
	void displayedOrdersTest() {
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 2);
		while (opHelper.getPendingSendOrders().toArray(IOrderData[]::new).length < clientModel.getAllPendingSendOrders().length) {
			clientView.refreshOrders();
		}
		Assertions.assertEquals(opHelper.getPendingSendOrders().toArray(IOrderData[]::new).length, 2);
		
		GeneralTestUtilityClass.arrayContentEquals(opHelper.getPendingSendOrders().toArray(IOrderData[]::new),
				clientModel.getAllPendingSendOrders());
		
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 1);
		while (opHelper.getSentOrders().toArray(IOrderData[]::new).length < clientModel.getAllSentOrders().length) {
			clientView.refreshOrders();
		}
		Assertions.assertEquals(opHelper.getSentOrders().toArray(IOrderData[]::new).length, 1);
		
		GeneralTestUtilityClass.arrayContentEquals(opHelper.getSentOrders().toArray(IOrderData[]::new),
				clientModel.getAllSentOrders());
	}
}