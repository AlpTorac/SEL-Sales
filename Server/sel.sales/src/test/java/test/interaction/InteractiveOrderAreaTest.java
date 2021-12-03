package test.interaction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import model.dish.DishMenu;
import model.dish.DishMenuItemData;
import model.order.OrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.client.view.StandardClientViewOperationsUtilityClass;
import test.external.dummy.DummyClientExternal;
import test.external.dummy.DummyConnectionUtility;
import test.external.dummy.DummyStandardClient;
import test.external.dummy.DummyStandardInteraction;
import test.external.dummy.DummyStandardServer;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

@Disabled("Takes too long to finish")
//@Execution(value = ExecutionMode.SAME_THREAD)
class InteractiveOrderAreaTest extends ApplicationTest {
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
	
	private String serviceID = "serviceID";
	private String serviceName = "serviceName";
	private String deviceName = "deviceName";
	private String deviceAddress = "deviceAddress";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private StandardClientViewOperationsUtilityClass clientOpHelper;
	
	private DummyStandardServer server;
	private String serverName = "serverName";
	private String serverAddress = "serverAddress";
	private DummyConnectionUtility connUtilServer;
	
	private DummyStandardClient client;
	private String clientName = "clientName";
	private String clientAddress = "clientAddress";
	private DummyConnectionUtility connUtilClient;
	
	private DummyStandardInteraction interaction;
	
	private IClientView clientView;
	
	private DishMenu menu;
	
	private volatile boolean actionFinished = false;
	
	private String o1id;
	private String o2id;
	private String o3id;
	private String o4id;
	private String o5id;
	private String[] oids;
	
	private String so1;
	private String so2;
	private String so3;
	private String so4;
	private String so5;
	private String[] sos;
	
	private OrderData od1;
	private OrderData od2;
	private OrderData od3;
	private OrderData od4;
	private OrderData od5;
	private OrderData[] ods;
	
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
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		this.connUtilServer = new DummyConnectionUtility(this.serverAddress);
		this.connUtilClient = new DummyConnectionUtility(this.clientAddress);
		
		server = new DummyStandardServer(serverName, serverAddress, connUtilServer);
		client = new DummyStandardClient(clientName, clientAddress, connUtilClient);
		
		interaction = new DummyStandardInteraction(server, client);
		interaction.connectPartakers(server, client);
		
		menu = server.createDishMenu();
		menu.addMenuItem(server.createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1id));
		menu.addMenuItem(server.createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2id));
		menu.addMenuItem(server.createDishMenuItem(i3Name, i3PorSize, i3ProCost, i3Price, i3id));
		
		interaction.broadcastMenu(menu);
		
		item1 = client.getModel().getMenuItem(i1id);
		item2 = client.getModel().getMenuItem(i2id);
		item3 = client.getModel().getMenuItem(i3id);
		
		o1id = "order1";
		o2id = "order2";
		o3id = "order3";
		o4id = "order4";
		o5id = "order5";
		
		so1 = o1id + "#20200809235959890#0#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1;";
		so2 = o2id + "#20200809235959890#0#0:item2,3;item3,5;item1,7;item2,0;item3,1;";
		so3 = o3id + "#20200809235959890#0#0:item3,5;item1,7;item2,0;item3,1;";
		so4 = o4id + "#20200809235959890#0#0:item1,7;item2,0;item3,1;";
		so5 = o5id + "#20200809235959890#0#0:item1,2;item2,3;item3,5;";
		
		od1 = client.getModel().getOrderHelper().deserialiseOrderData(so1);
		od2 = client.getModel().getOrderHelper().deserialiseOrderData(so2);
		od3 = client.getModel().getOrderHelper().deserialiseOrderData(so3);
		od4 = client.getModel().getOrderHelper().deserialiseOrderData(so4);
		od5 = client.getModel().getOrderHelper().deserialiseOrderData(so5);
		
		oids = new String[] {o1id, o2id, o3id, o4id, o5id};
		sos = new String[] {so1, so2, so3, so4, so5};
		ods = new OrderData[] {od1, od2, od3, od4, od5};
		
		runFXAction(()->{
			clientView = new StandardClientView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), client.getController(), client.getModel());
			clientView.startUp();
			
			clientOpHelper = new StandardClientViewOperationsUtilityClass(
					clientView, client.getController(), client.getModel());
		});
	}

	@AfterEach
	void cleanUp() {
		interaction.close();
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
	}
	
	private boolean orderDatasEqual(OrderData od1, OrderData od2) {
		return GeneralTestUtilityClass.arrayContentEquals(od1.getOrderedItems(), od2.getOrderedItems())
				&& od1.getID().serialisedIDequals(od2.getID().toString());
	}
	
	@SuppressWarnings("resource")
	@Test
	void orderCycleTest() {
		for (int i = 0; i < ods.length; i++) {
			final int index = i;
			final boolean isCash = GeneralTestUtilityClass.generateRandomBoolean();
			final boolean isHere = GeneralTestUtilityClass.generateRandomBoolean();
			
			runFXAction(()->{clientOpHelper.orderTakingAreaDisplayOrder(ods[index]);});
			Assertions.assertTrue(this.orderDatasEqual(ods[i], clientOpHelper.getOrderTakingAreaCurrentOrder()));
			
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addCookingOrder(ods[index].getID().toString());});
			
//			Assertions.assertTrue(clientOpHelper.getCookingOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));

			Assertions.assertTrue(client.getModel().getCookingOrder(ods[i].getID().toString()) != null);
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addPendingPaymentOrder(ods[index].getID().toString());});
//			Assertions.assertTrue(clientOpHelper.getPendingPaymentOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
			
			Assertions.assertTrue(client.getModel().getPendingPaymentOrder(ods[i].getID().toString()) != null);
			Assertions.assertEquals(client.getModel().getAllSentOrders().length, i);
//			Assertions.assertEquals(client.getModel().getAllWrittenOrders().length, i);
			
			runFXAction(()->{clientOpHelper.addPendingSendOrder(ods[index].getID().toString(), isCash, isHere);});
//			Assertions.assertTrue(clientOpHelper.getPendingSendOrders().stream()
//					.anyMatch(ordData -> this.orderDatasEqual(ordData, ods[index])));
			
			Assertions.assertTrue(client.getModel().getAllPendingSendOrders().length == 1
					|| client.getModel().getSentOrder(ods[i].getID().toString()) != null);
			
			this.orderDatasEqual(server.getModel().getOrder(ods[i].getID().toString()), ods[i]);
			
			System.out.println("------------------------------- order " + index + " sent -------------------------------");
		}
	}
}
