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

import client.controller.ClientController;
import client.controller.IClientController;
import client.controller.StandardClientController;
import client.external.IClientExternal;
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
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientExternal;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderTakingAreaTest extends ApplicationTest {
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
			
			data = clientModel.getOrderHelper().deserialiseOrderData("order2#20200809235959866#1#0:item1,2;item1,2;item1,2;item1,2;item2,3;");
			
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
	void orderInputTest() {
		Assertions.assertEquals(opHelper.getOrderTakingAreaEntryCount(), 0);
		
		// 2x item2
		runFXAction(()->{opHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(opHelper.getOrderTakingAreaEntryCount(), 1);
		runFXAction(()->{
			opHelper.orderTakingAreaSetEntryItem(item2, 0);
			opHelper.orderTakingAreaIncEntryAmount(0);
		});
		
		// 2x item2, 1x item3
		runFXAction(()->{opHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(opHelper.getOrderTakingAreaEntryCount(), 2);
		runFXAction(()->{
			opHelper.orderTakingAreaSetEntryItem(item3, 1);
		});
		
		// 2x item1, 2x item2, 1x item3
		runFXAction(()->{opHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(opHelper.getOrderTakingAreaEntryCount(), 3);
		runFXAction(()->{
			opHelper.orderTakingAreaSetEntryItem(item1, 2);
			opHelper.orderTakingAreaIncEntryAmount(2);
		});
		
		// 2x item1, 1x item1, 1x item3
		runFXAction(()->{
			opHelper.orderTakingAreaSetEntryItem(item1, 0);
			opHelper.orderTakingAreaDecEntryAmount(0);
		});
		
		// 2x item1, 1x item1, 1x item3
		runFXAction(()->{opHelper.orderTakingAreaNewEntry();});
		Assertions.assertEquals(opHelper.getOrderTakingAreaEntryCount(), 4);
		runFXAction(()->{
			opHelper.orderTakingAreaSetEntryItem(item2, 3);
		});
		runFXAction(()->{opHelper.orderTakingAreaRemoveEntry(3);});
		
		AccumulatingOrderItemAggregate d1 = clientModel.getOrderHelper().createOrderItem(item1, BigDecimal.valueOf(2));
		AccumulatingOrderItemAggregate d2 = clientModel.getOrderHelper().createOrderItem(item1, BigDecimal.valueOf(1));
		AccumulatingOrderItemAggregate d3 = clientModel.getOrderHelper().createOrderItem(item3, BigDecimal.valueOf(1));
		
		GeneralTestUtilityClass.arrayContentEquals(opHelper.getOrderTakingAreaCurrentOrderItems(),
				new AccumulatingOrderItemAggregate[] {d1,d2,d3});
		
		GeneralTestUtilityClass.arrayContentEquals(clientModel.getOrderHelper().deserialiseOrderData(opHelper.getOrderTakingAreaSerialisedOrder()).getOrderedItems(),
				new AccumulatingOrderItemAggregate[] {d1,d2,d3});
	}
	
	@Test
	void displayOrderTest() {
		runFXAction(()->{opHelper.orderTakingAreaDisplayOrder(data);});
		
		GeneralTestUtilityClass.arrayContentEquals(opHelper.getOrderTakingAreaCurrentOrderItems(),
				data.getOrderedItems());
		
		GeneralTestUtilityClass.arrayContentEquals(clientModel.getOrderHelper().deserialiseOrderData(opHelper.getOrderTakingAreaSerialisedOrder()).getOrderedItems(),
				data.getOrderedItems());
	}
}
