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
import client.view.composites.CookingOrderEntry;
import client.view.composites.OrderEntry;
import client.view.composites.PastOrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.DishMenuItemData;
import model.order.OrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class PastOrderEntryTest extends ApplicationTest {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
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
	
	private PriceUpdateTarget<OrderEntry> u;
	private PastOrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serialisedOrder;
	private OrderData orderData;
	
	@BeforeEach
	void prep() {
		clientModel = new ClientModel(this.testFolderAddress);
		serverModel = new ServerModel(this.testFolderAddress);
		controller = new StandardClientController(clientModel);
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		serialisedOrder = "order1#20200809112233000#0#1:item1,2;";
		orderData = this.serverModel.getOrderHelper().deserialiseOrderData(serialisedOrder);
		
		serverModel.addOrder(serialisedOrder);
		
		clientModel.setDishMenu(serverModel.getMenuData());
		clientModel.addCookingOrder(serialisedOrder);
		clientModel.makePendingPaymentOrder(orderData.getID().toString());
		clientModel.makePendingSendOrder(serialisedOrder);
		
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
		
		entry = new PastOrderEntry(controller, new FXUIComponentFactory(), u, orderData);
	}

	@AfterEach
	void cleanUp() {
		serverModel.close();
		entry = null;
		u = null;
	}
	
	@Test
	void orderDisplayTest() {
		entry.refreshMenu(serverModel.getMenuData());
		entry.displayData(orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		OrderData newData = serverModel.getOrderHelper().deserialiseOrderData(entry.getCurrentOrder());
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
	}
}
