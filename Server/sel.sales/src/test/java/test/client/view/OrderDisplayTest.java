package test.client.view;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.OrderList;
import client.view.composites.OrderEntry;
import model.order.OrderData;
import server.model.IServerModel;
import test.FXTestTemplate;
import view.repository.uifx.FXUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderDisplayTest extends FXTestTemplate {
	private IClientModel clientModel;
	private IServerModel serverModel;
	private IClientController controller;
	
	private OrderList accordion;
	
	private ArrayList<OrderEntry> createdEntries;
	
	private UIComponentFactory fac = new FXUIComponentFactory();
	
	@BeforeEach
	void prep() {
		createdEntries = new ArrayList<OrderEntry>();
		
		clientModel = this.initClientModel();
		serverModel = this.initServerModel();
		controller = this.initClientController(clientModel);
		this.initDishMenuItems(serverModel);
		this.addDishMenuToServerModel(serverModel);
		this.getServerMenuIntoClient(serverModel, clientModel);
		this.initOrders(clientModel);
		
		clientModel.addOrder(oData1);
		clientModel.addOrder(oData2);
		clientModel.addOrder(oData3);
		
		accordion = new OrderList(controller, fac) {
			@Override
			protected OrderEntry createOrderEntry(OrderData data) {
				OrderEntry e = new OrderEntry(controller, fac, accordion, data);
				createdEntries.add(e);
				return e;
			}
		};
	}

	@AfterEach
	void cleanUp() {
		this.closeAll(serverModel, clientModel);
	}
	
	@Test
	void addOrderDataTest() {
		accordion.addOrderData(oData1);
		Collection<OrderEntry> col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		col.clear();
		
		accordion.addOrderData(oData2);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		
		accordion.addOrderData(oData2);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		
		accordion.addOrderData(oData3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
	}
	
	@Test
	void cloneOrderEntriesTest() {
		Collection<OrderEntry> col = accordion.cloneOrderEntries();
		accordion.addOrderData(oData1);
		accordion.addOrderData(oData2);
		accordion.addOrderData(oData3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
	}
	
	@Test
	void removeTest() {
		Collection<OrderEntry> col;
		accordion.addOrderData(oData1);
		accordion.addOrderData(oData2);
		accordion.addOrderData(oData3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get(), oData1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(oData1.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get(), oData2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(oData2.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get(), oData3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(oData3.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 0);
	}
}
