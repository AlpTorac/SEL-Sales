package test.client.view;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

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
import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import client.view.composites.OrderTakingAreaOrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderTakingAreaOrderEntryTest extends ApplicationTest {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
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
	
	private PriceUpdateTarget<OrderEntry> u;
	private OrderTakingAreaOrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serialisedOrder;
	private IOrderData orderData;
	
	@BeforeEach
	void prep() {
		clientModel = new ClientModel(this.testFolderAddress);
		serverModel = new ServerModel(this.testFolderAddress);
		controller = new StandardClientController(clientModel);
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		serialisedOrder = "order1#20200809112233000#0#0:item1,2;";
		orderData = this.serverModel.getOrderHelper().deserialiseOrderData(serialisedOrder);
		
		serverModel.addOrder(serialisedOrder);
		
		item1 = serverModel.getMenuItem(i1id);
		item2 = serverModel.getMenuItem(i2id);
		item3 = serverModel.getMenuItem(i3id);
		
		isPriceRefreshed = false;
		isRemoved = false;
		
		u = new PriceUpdateTarget<OrderEntry>() {
			@Override
			public void refreshPrice() {
				isPriceRefreshed = true;
			}

			@Override
			public void remove(OrderEntry referenceOfCaller) {
				isRemoved = true;
			}
		};
		
		clientModel.setDishMenu(serverModel.getMenuData());
		entry = new OrderTakingAreaOrderEntry(controller, new FXUIComponentFactory(), u);
	}

	@AfterEach
	void cleanUp() {
		serverModel.close();
		entry = null;
		u = null;
	}

	@Test
	void initialDataTest() {
		entry = new OrderTakingAreaOrderEntry(controller, new FXUIComponentFactory(), u, orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
	}
	
	@Test
	void nextTabTest() {
		Assertions.assertFalse(isPriceRefreshed);
		entry.refreshMenu(serverModel.getMenuData());
		Assertions.assertTrue(isPriceRefreshed);
		isPriceRefreshed = false;
		entry.displayData(orderData);
		Assertions.assertTrue(isPriceRefreshed);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		IOrderData newData = serverModel.getOrderHelper().deserialiseOrderData(entry.serialiseCurrentOrder());
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
		
		entry.getNextTabButton().performArtificialClick();
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void addNewMenuItemEntryTest() {
		entry.refreshMenu(serverModel.getMenuData());
		entry.displayData(orderData);
		
		Collection<MenuItemEntry> mieCol = entry.cloneMenuItemEntries();
		int orderedItemsLen = orderData.getOrderedItems().length;
		
		Assertions.assertEquals(mieCol.size(), orderedItemsLen);
		
		entry.getAddMenuItemButton().performArtificialClick();
		mieCol = entry.cloneMenuItemEntries();
		Assertions.assertEquals(mieCol.size(), orderedItemsLen + 1);
	}
}