package test.client.view;

import static org.junit.jupiter.api.Assertions.*;

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

import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.AccumulatingOrderItemAggregate;
import server.controller.IServerController;
import server.controller.ServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.model.order.OrderTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderEntryTest extends ApplicationTest {
	private IServerModel model;
	private IServerController controller;
	
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
	private OrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serialisedOrder;
	private IOrderData orderData;
	
	@BeforeEach
	void prep() {
		model = new ServerModel(this.testFolderAddress);
		controller = new StandardServerController(model);
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		serialisedOrder = "order1#20200809112233000#0#0:item1,2;";
		orderData = this.model.getOrderHelper().deserialiseOrderData(serialisedOrder);
		
		model.addOrder(serialisedOrder);
		
		item1 = model.getMenuItem(i1id);
		item2 = model.getMenuItem(i2id);
		item3 = model.getMenuItem(i3id);
		
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
		entry = new OrderEntry(controller, new FXUIComponentFactory(), u);
	}

	@AfterEach
	void cleanUp() {
		model.close();
		entry = null;
		u = null;
	}

	@Test
	void orderDisplayTest() {
		Assertions.assertFalse(isPriceRefreshed);
		entry.refreshMenu(model.getMenuData());
		Assertions.assertTrue(isPriceRefreshed);
		isPriceRefreshed = false;
		entry.displayData(orderData);
		Assertions.assertTrue(isPriceRefreshed);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		IOrderData newData = model.getOrderHelper().deserialiseOrderData(entry.serialiseCurrentOrder());
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
	}
	
	@Test
	void refreshMenuNullTest() {
		Assertions.assertNull(entry.getActiveMenu());
		entry.refreshMenu(null);
		Assertions.assertFalse(isPriceRefreshed);
		Assertions.assertNull(entry.getActiveMenu());
	}
	
	@Test
	void refreshMenuTest() {
		Assertions.assertNull(entry.getActiveMenu());
		IDishMenuData menu = model.getMenuData();
		isPriceRefreshed = false;
		entry.refreshMenu(menu);
		Assertions.assertTrue(isPriceRefreshed);
		Assertions.assertEquals(entry.getActiveMenu(), menu);
	}
	
	@Test
	void resetUserInputTest() {
		Assertions.assertFalse(isPriceRefreshed);
		entry.refreshMenu(model.getMenuData());
		Assertions.assertTrue(isPriceRefreshed);
		isPriceRefreshed = false;
		entry.displayData(orderData);
		Assertions.assertTrue(isPriceRefreshed);
		
		Collection<MenuItemEntry> col = entry.cloneMenuItemEntries();
		Assertions.assertEquals(col.size(), orderData.getOrderedItems().length);
		
		isPriceRefreshed = false;
		entry.resetUserInput();
		Assertions.assertTrue(isPriceRefreshed);
		
		col = entry.cloneMenuItemEntries();
		Assertions.assertEquals(col.size(), 0);
	}
	
	@Test
	void cloneTest() {
		OrderEntry clone = entry.clone();
		
		Assertions.assertFalse(clone == entry);
		
		Assertions.assertEquals(entry.getActiveData(), clone.getActiveData());
		Assertions.assertEquals(entry.getActiveMenu(), clone.getActiveMenu());
		
		Collection<MenuItemEntry> col = entry.cloneMenuItemEntries();
		Collection<MenuItemEntry> cloneCol = clone.cloneMenuItemEntries();
		
		col.containsAll(cloneCol);
		cloneCol.containsAll(col);
	}
	
	@Test
	void removeTest() {
		entry.removeFromParent();
		Assertions.assertTrue(isRemoved);
	}
	
	@Test
	void initialDataTest() {
		entry = new OrderEntry(controller, new FXUIComponentFactory(), u, orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		IOrderData newData = model.getOrderHelper().deserialiseOrderData(entry.serialiseCurrentOrder());
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
	}
}
