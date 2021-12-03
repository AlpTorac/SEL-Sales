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
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClientExternal;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class PendingPaymentOrdersAreaTest extends ApplicationTest {
	private DishMenuItemData item1;
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private DishMenuItemData item2;
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private DishMenuItemData item3;
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
	
	private OrderData data;
	
	private String o1id = "order2";
	private String o2id = "order6";
	private String o3id = "order7";
	
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
			
			clientModel.addCookingOrder(o1id+"#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
			clientModel.addCookingOrder(o2id+"#20200813000000183#1#1:item3,5;item3,4;");
			clientModel.addCookingOrder(o3id+"#20200909112233937#0#0:item1,2;item2,5;");
			
			clientModel.makePendingPaymentOrder(o1id);
			clientModel.makePendingPaymentOrder(o2id);
			clientModel.makePendingPaymentOrder(o3id);
			
			clientView.refreshOrders();
			
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
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 3);
		
		while (opHelper.getPendingPaymentOrders().toArray(OrderData[]::new).length < clientModel.getAllPendingPaymentOrders().length) {
			clientView.refreshOrders();
		}
		
		Assertions.assertEquals(opHelper.getPendingPaymentOrders().toArray(OrderData[]::new).length, 3);
		
		GeneralTestUtilityClass.arrayContentEquals(opHelper.getPendingPaymentOrders().toArray(OrderData[]::new),
				clientModel.getAllPendingPaymentOrders());
	}
	
	@Test
	void optionsTest() {
		this.displayedOrdersTest();
		
		boolean o1IsCash = false;
		boolean o1IsHere = true;
		runFXAction(()->{
			opHelper.ppoaSetPaymentOption(o1id, o1IsCash);
			opHelper.ppoaSetPlaceOption(o1id, o1IsHere);
		});
		Assertions.assertTrue(opHelper.ppoaGetIsCash(o1id) == o1IsCash);
		
		
		boolean o2IsCash = false;
		boolean o2IsHere = false;
		runFXAction(()->{
			opHelper.ppoaSetPaymentOption(o2id, o2IsCash);
			opHelper.ppoaSetPlaceOption(o2id, o2IsHere);
		});
		Assertions.assertTrue(opHelper.ppoaGetIsCash(o2id) == o2IsCash);
		Assertions.assertTrue(opHelper.ppoaGetIsHere(o2id) == o2IsHere);
		
		boolean o3IsCash = true;
		boolean o3IsHere = false;
		runFXAction(()->{
			opHelper.ppoaSetPlaceOption(o3id, o3IsHere);
			opHelper.ppoaSetPaymentOption(o3id, o3IsCash);
		});
		Assertions.assertTrue(opHelper.ppoaGetIsHere(o3id) == o3IsHere);
	}
	
	@Test
	void nextTabTest() {
		this.optionsTest();
		
		OrderData d1PPOA = clientModel.getOrderHelper().deserialiseOrderData(opHelper.ppoaGetSerialisedOrder(o1id));
		Assertions.assertFalse(d1PPOA.getIsCash());
		Assertions.assertTrue(d1PPOA.getIsHere());
		
		OrderData d2PPOA = clientModel.getOrderHelper().deserialiseOrderData(opHelper.ppoaGetSerialisedOrder(o2id));
		Assertions.assertFalse(d2PPOA.getIsCash());
		Assertions.assertFalse(d2PPOA.getIsHere());
		
		OrderData d3PPOA = clientModel.getOrderHelper().deserialiseOrderData(opHelper.ppoaGetSerialisedOrder(o3id));
		Assertions.assertTrue(d3PPOA.getIsCash());
		Assertions.assertFalse(d3PPOA.getIsHere());
		
		OrderData[] dPPOA = new OrderData[] {d1PPOA, d2PPOA, d3PPOA};
		OrderData[] dModel = clientModel.getAllPendingPaymentOrders();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dPPOA, dModel,
				(o1,o2)->{return o1.itemsEqual(o2);}));
	}
}
