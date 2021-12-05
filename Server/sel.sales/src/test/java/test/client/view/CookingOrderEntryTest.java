package test.client.view;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.CookingOrderEntry;
import client.view.composites.OrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.order.OrderData;
import server.model.IServerModel;
import test.GeneralTestUtilityClass;
import test.FXTestTemplate;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class CookingOrderEntryTest extends FXTestTemplate {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
	private PriceUpdateTarget<OrderEntry> u;
	private CookingOrderEntry entry;
	
	private boolean isPriceRefreshed;
	private boolean isRemoved;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private OrderData orderData;
	
	@BeforeEach
	void prep() {
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		clientModel = this.initClientModel();
		serverModel = this.initServerModel();
		controller = this.initClientController(clientModel);
		this.addDishMenuToServerModel(serverModel);
		this.initOrders(serverModel);
		orderData = oData2;
		
		serverModel.addOrder(orderData);
		
		clientModel.setDishMenu(serverModel.getMenuData());
		clientModel.addCookingOrder(orderData);
		
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
		
		entry = new CookingOrderEntry(controller, new FXUIComponentFactory(), u, orderData);
	}

	@AfterEach
	void cleanUp() {
		this.closeModel(serverModel);
		this.closeModel(clientModel);
		entry = null;
		u = null;
	}
	
	@Test
	void nextTabTest() {
		entry.refreshMenu(serverModel.getMenuData());
		entry.displayData(orderData);
		
		Assertions.assertEquals(entry.getActiveData(), orderData);
		Assertions.assertEquals(entry.getSerialisedOrderID(), orderData.getID().toString());
		
		OrderData newData = entry.getCurrentOrder();
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(
				newData.getOrderedItems(),
				orderData.getOrderedItems()));
		Assertions.assertEquals(newData.getIsCash(), false);
		Assertions.assertEquals(newData.getIsHere(), false);
		Assertions.assertEquals(newData.getIsDiscounted(), orderData.getIsDiscounted());
		
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(entry, orderData);
		
		entry.getNextTabButton().performArtificialClick();
		
		Assertions.assertEquals(clientModel.getAllCookingOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllPendingPaymentOrders().length, 1);
		Assertions.assertEquals(clientModel.getAllPendingSendOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 0);
		Assertions.assertEquals(clientModel.getAllWrittenOrders().length, 1);
	}
	
	@Test
	void editTest() {
		entry.refreshMenu(serverModel.getMenuData());
		entry.displayData(orderData);
		
		entry.getEditButton().performArtificialClick();
		
		Assertions.assertEquals(clientModel.getEditTarget(), orderData);
	}
}
