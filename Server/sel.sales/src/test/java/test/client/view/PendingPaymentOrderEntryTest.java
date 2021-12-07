package test.client.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.OrderEntry;
import client.view.composites.PendingPaymentOrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class PendingPaymentOrderEntryTest extends FXTestTemplate {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
	private PriceUpdateTarget<OrderEntry> u;
	private PendingPaymentOrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
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
		this.getServerMenuIntoClient(serverModel, clientModel);
		
		orderData = oData1;
		
		serverModel.addOrder(orderData);
		
		clientModel.addCookingOrder(orderData);
		clientModel.makePendingPaymentOrder(orderData.getID().toString());
		
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
		
		entry = new PendingPaymentOrderEntry(controller, new FXUIComponentFactory(), u, orderData);
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
		entry = null;
		u = null;
	}
	
	@Test
	void nextTabTest() {
		entry.displayData(orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		OrderData newData = entry.getCurrentOrder();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), entry.getCashRadioButton().isToggled());
		Assertions.assertEquals(newData.getIsHere(), entry.getHereRadioButton().isToggled());
		Assertions.assertEquals(newData.getIsCash(), !entry.getCardRadioButton().isToggled());
		Assertions.assertEquals(newData.getIsHere(), !entry.getToGoRadioButton().isToggled());
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, newData);
		
		entry.getNextTabButton().performArtificialClick();
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void editTest() {
		entry.displayData(orderData);
		
		entry.getEditButton().performArtificialClick();
		
		Assertions.assertTrue(this.ordersEqual(clientModel.getEditTarget(), orderData));
	}
	
	@Test
	void optionsTest() {
		entry.displayData(orderData);
		OrderData newData;
		boolean isCash;
		boolean isHere;
		
		isCash = true;
		isHere = true;
		entry.getCashRadioButton().setToggled(isCash);
		entry.getHereRadioButton().setToggled(isHere);
		newData = entry.getCurrentOrder();
		Assertions.assertEquals(newData.getIsCash(), isCash);
		Assertions.assertEquals(newData.getIsHere(), isHere);
		
		isCash = false;
		isHere = true;
		entry.getCashRadioButton().setToggled(isCash);
		entry.getHereRadioButton().setToggled(isHere);
		newData = entry.getCurrentOrder();
		Assertions.assertEquals(newData.getIsCash(), isCash);
		Assertions.assertEquals(newData.getIsHere(), isHere);
		
		isCash = true;
		isHere = false;
		entry.getCashRadioButton().setToggled(isCash);
		entry.getHereRadioButton().setToggled(isHere);
		newData = entry.getCurrentOrder();
		Assertions.assertEquals(newData.getIsCash(), isCash);
		Assertions.assertEquals(newData.getIsHere(), isHere);
		
		isCash = false;
		isHere = false;
		entry.getCashRadioButton().setToggled(isCash);
		entry.getHereRadioButton().setToggled(isHere);
		newData = entry.getCurrentOrder();
		Assertions.assertEquals(newData.getIsCash(), isCash);
		Assertions.assertEquals(newData.getIsHere(), isHere);
	}
}