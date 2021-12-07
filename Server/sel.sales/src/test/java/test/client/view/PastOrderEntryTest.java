package test.client.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.OrderEntry;
import client.view.composites.PastOrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class PastOrderEntryTest extends FXTestTemplate {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
	private PriceUpdateTarget<OrderEntry> u;
	private PastOrderEntry entry;
	
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
		
		this.getServerMenuIntoClient(serverModel, clientModel);
		clientModel.addCookingOrder(orderData);
		clientModel.makePendingPaymentOrder(orderData.getID().toString());
		clientModel.makePendingSendOrder(orderData);
		
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
		this.closeAll(serverModel, clientModel);
		entry = null;
		u = null;
	}
	
	@Test
	void orderDisplayTest() {
		entry.refreshMenu(serverModel.getMenuData());
		entry.displayData(orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		OrderData newData = entry.getCurrentOrder();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
//		Assertions.assertEquals(newData.getIsCash(), orderData.getIsCash());
//		Assertions.assertEquals(newData.getIsHere(), orderData.getIsHere());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
	}
}
