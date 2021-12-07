package test.client.view;

import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.MenuItemEntry;
import client.view.composites.OrderEntry;
import client.view.composites.OrderTakingAreaOrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderTakingAreaOrderEntryTest extends FXTestTemplate {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
	private PriceUpdateTarget<OrderEntry> u;
	private OrderTakingAreaOrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String serialisedOrder;
	private OrderData orderData;
	
	@BeforeEach
	void prep() {
		clientModel = this.initClientModel();
		serverModel = this.initServerModel();
		controller = this.initClientController(clientModel);
		this.initDishMenuItems(serverModel);
		this.addDishMenuToServerModel(serverModel);
		this.initOrders(clientModel);
		this.getPrivateFieldsFromModel(clientModel);
		
		serialisedOrder = orderDAO.serialiseValueObject(oData1);
		orderData = orderDAO.parseValueObject(serialisedOrder);
		
		serverModel.addOrder(serialisedOrder);
		
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
		this.getServerMenuIntoClient(serverModel, clientModel);
		entry = new OrderTakingAreaOrderEntry(controller, new FXUIComponentFactory(), u);
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
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
		
		OrderData newData = entry.getCurrentOrder();
		
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