package test.client.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import client.view.composites.CookingOrderAccordion;
import client.view.composites.CookingOrderEntry;
import client.view.composites.OrderAccordion;
import client.view.composites.OrderEntry;
import client.view.composites.PriceUpdateTarget;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderAccordionTest extends ApplicationTest {
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
	
	private OrderAccordion accordion;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String so1;
	private String so2;
	private String so3;
	
	private IOrderData od1;
	private IOrderData od2;
	private IOrderData od3;
	
	private ArrayList<OrderEntry> createdEntries;
	
	private UIComponentFactory fac = new FXUIComponentFactory();
	
	@BeforeEach
	void prep() {
		createdEntries = new ArrayList<OrderEntry>();
		
		clientModel = new ClientModel(this.testFolderAddress);
		serverModel = new ServerModel(this.testFolderAddress);
		controller = new StandardClientController(clientModel);
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		serverModel.addMenuItem(serverModel.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		item1 = serverModel.getMenuItem(i1id);
		item2 = serverModel.getMenuItem(i2id);
		item3 = serverModel.getMenuItem(i3id);
		
		clientModel.setDishMenu(serverModel.getMenuData());
		
		so1 = "order1#20200809112233000#0#0:item1,2;";
		od1 = this.clientModel.getOrderHelper().deserialiseOrderData(so1);
		
		so2 = "order2#20200809235959866#1#0:item1,2;item2,3;";
		od2 = this.clientModel.getOrderHelper().deserialiseOrderData(so2);
		
		so3 = "order3#20200809000000675#1#1:item3,5;";
		od3 = this.clientModel.getOrderHelper().deserialiseOrderData(so3);
		
		clientModel.addOrder(so1);
		clientModel.addOrder(so2);
		clientModel.addOrder(so3);
		
		accordion = new OrderAccordion(controller, fac) {
			@Override
			protected OrderEntry createOrderEntry(IOrderData data) {
				OrderEntry e = new OrderEntry(controller, fac, accordion, data);
				createdEntries.add(e);
				return e;
			}
		};
	}

	@AfterEach
	void cleanUp() {
		serverModel.close();
		clientModel.close();
	}
	
	@Test
	void addOrderDataTest() {
		accordion.addOrderData(od1);
		Collection<OrderEntry> col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		col.clear();
		
		accordion.addOrderData(od2);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		
		accordion.addOrderData(od2);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		
		accordion.addOrderData(od3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
	}
	
	@Test
	void cloneOrderEntriesTest() {
		Collection<OrderEntry> col = accordion.cloneOrderEntries();
		accordion.addOrderData(od1);
		accordion.addOrderData(od2);
		accordion.addOrderData(od3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
	}
	
	@Test
	void removeTest() {
		Collection<OrderEntry> col;
		accordion.addOrderData(od1);
		accordion.addOrderData(od2);
		accordion.addOrderData(od3);
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 3);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get(), od1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(od1.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get(), od2);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(od2.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 1);
		OrderEntryUtilityClass.assertOrderEntryDisplayEquals(col.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get(), od3);
		
		createdEntries.stream().filter(e -> e.getActiveData().getID().equals(od3.getID())).findFirst().get().removeFromParent();
		col = accordion.cloneOrderEntries();
		Assertions.assertEquals(col.size(), 0);
	}
}
