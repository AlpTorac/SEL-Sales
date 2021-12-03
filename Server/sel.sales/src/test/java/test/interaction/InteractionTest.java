package test.interaction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.external.IClientExternal;
import client.model.ClientModel;
import client.model.IClientModel;
import external.device.IDevice;
import model.connectivity.IDeviceData;
import model.dish.DishMenu;
import model.dish.DishMenuData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.external.IServerExternal;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientExternal;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyInteraction;
import test.external.dummy.DummyServer;
import test.external.dummy.DummyConnectivityTestWrapper;
import test.external.dummy.DummyServerExternal;
import test.external.dummy.InteractionUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class InteractionTest {
	private long waitTime = 100;
	
//	private DummyDevice client;
	private String clientAddress = "clientAddress";
	private String clientName = "clientName";
//	private IClientModel clientModel;
//	private IClientController clientController;
//	private DummyClientExternal clientExternal;
	
//	private DummyDevice server;
	private String serverAddress = "serverAddress";
	private String serverName = "serverName";
//	private IServerModel serverModel;
//	private IServerController serverController;
//	private DummyServerExternal serverExternal;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private BigDecimal o1a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a2 = BigDecimal.valueOf(3);
	private BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	private String o1id = "order1";
	private String o2id = "order2";
	private String o3id = "order3";
	
	private String so1;
	private String so2;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serviceID = "serviceID";
	private String serviceName = "serviceName";
	
	private DishMenu menu;
	
	private DummyInteraction interaction;
	
	private DummyServer server;
	private DummyClient client;
	
	private String serialisedTableNumbers = "1-2,4,5,1-10,90,11";
	private String serialisedTableNumbers2 = "1-5";
	
	@BeforeEach
	void prep() {
		server = new DummyServer(serviceID, serviceName, serverName, serverAddress);
		client = new DummyClient(serviceID, serviceName, clientName, clientAddress);
		
		interaction = new DummyInteraction(server, client);
		interaction.connectPartakers(server, client);
		
		menu = server.createDishMenu();
		menu.addMenuItem(server.createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1id));
		menu.addMenuItem(server.createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2id));
		menu.addMenuItem(server.createDishMenuItem(i3Name, i3PorSize, i3ProCost, i3Price, i3id));
		
		so1 = o1id+"#20200809235959866#1#0:item1,2;item2,3;";
		so2 = o2id+"#20200809235959866#1#0:item1,2;item2,3;item3,5;";
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	@Test
	void deviceFamiliarisingTest() {
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData().length, 1);
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceName(), clientName);
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), clientAddress);
//		
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData().length, 1);
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceName(), serverName);
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), serverAddress);
//		
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData().length, 1);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceName(), clientName);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceAddress(), clientAddress);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
//		
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData().length, 1);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceName(), serverName);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceAddress(), serverAddress);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
		Assertions.assertEquals(server.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(server.getAllDiscoveredDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(server.getAllDiscoveredDeviceData()[0].getDeviceAddress(), clientAddress);
		
		Assertions.assertEquals(client.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(client.getAllDiscoveredDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(client.getAllDiscoveredDeviceData()[0].getDeviceAddress(), serverAddress);
		
		Assertions.assertEquals(server.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getDeviceAddress(), clientAddress);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
		
		Assertions.assertEquals(client.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getDeviceAddress(), serverAddress);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
	}
	
	@Test
	void connectionTest() {
//		clientExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
//		serverExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
		
		Assertions.assertEquals(client.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getIsConnected(), true);
		
		Assertions.assertEquals(server.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getIsConnected(), true);
	}
	
	@Test
	void menuExchangeTest() {
		interaction.setServerMenu(menu);
//		GeneralTestUtilityClass.performWait(100);
//		menuData = clientModel.getMenuData();
		
		DishMenuData menuData = client.getMenuData();
		while (menuData.getAllItems().length == 0) {
			interaction.reSetServerMenu();
			GeneralTestUtilityClass.performWait(waitTime);
			menuData = client.getMenuData();
		}
		Assertions.assertTrue(server.getMenuData().equals(menuData));
	}
	
	@Test
	void orderExchangeTest() {
		interaction.broadcastMenu(menu);
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(server.getAllUnconfirmedOrders()[0].equals(server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(client.getAllSentOrders()[0].equals(client.deserialiseOrderData(so1)));
	}
	
	@Test
	void multipleOrderExchangeTest() {
		interaction.broadcastMenu(menu);
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(server.getAllUnconfirmedOrders()[0].equals(server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(client.getAllSentOrders()[0].equals(client.deserialiseOrderData(so1)));
		
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(server.getAllUnconfirmedOrders()[0].equals(server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(client.getAllSentOrders()[0].equals(client.deserialiseOrderData(so1)));
		
		interaction.addPendingSendOrderToClient(client, o2id, so2);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 2);
		Assertions.assertTrue(server.getOrder(o2id).equals(server.deserialiseOrderData(so2)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 2);
		Assertions.assertTrue(client.getOrder(o2id).equals(client.deserialiseOrderData(so2)));
	}
	
	@Test
	void menuUpdateTest() {
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.removeMenuItem(i1id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.removeMenuItem(i2id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.addMenuItem(server.createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1Name));
		menu.addMenuItem(server.createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2Name));
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
	}
	
	@Test
	void tableNumberUpdateTest() {
		interaction.broadcastTableNumbers(serialisedTableNumbers);
		Assertions.assertTrue(server.tableNumbersEqual(client));
		
		interaction.broadcastTableNumbers(serialisedTableNumbers2);
		Assertions.assertTrue(server.tableNumbersEqual(client));
	}
}
