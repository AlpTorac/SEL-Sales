package test.client.view;

import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.DishMenuData;
import model.order.OrderData;
import server.controller.IServerController;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderEntryTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	
	private PriceUpdateTarget<OrderEntry> u;
	private OrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String serialisedOrder;
	private OrderData orderData;
	
	@BeforeEach
	void prep() {
		model = this.initServerModel();
		controller = this.initServerController(model);
		this.initDishMenuItems(model);
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
		this.getPrivateFieldsFromModel(model);
		
		serialisedOrder = orderDAO.serialiseValueObject(oData1);
		orderData = orderDAO.parseValueObject(serialisedOrder);
		
		model.addOrder(serialisedOrder);
		
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
		
		OrderData newData = entry.getCurrentOrder();
		
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
		DishMenuData menu = model.getMenuData();
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
		
		OrderData newData = entry.getCurrentOrder();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
	}
}
