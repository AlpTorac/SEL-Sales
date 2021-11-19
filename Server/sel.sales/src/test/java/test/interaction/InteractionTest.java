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
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.external.IServerExternal;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClientExternal;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyInteraction;
import test.external.dummy.DummyServerExternal;
import test.external.dummy.InteractionUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class InteractionTest {
	private ExecutorService es;
	
	private DummyDevice client;
	private String clientAddress = "clientAddress";
	private String clientName = "clientName";
	private IClientModel clientModel;
	private IClientController clientController;
	private DummyClientExternal clientExternal;
	
	private DummyDevice server;
	private String serverAddress = "serverAddress";
	private String serverName = "serverName";
	private IServerModel serverModel;
	private IServerController serverController;
	private DummyServerExternal serverExternal;
	
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
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serviceID = "serviceID";
	private String serviceName = "serviceName";
	
	private volatile DummyConnection clientToServer = null;
	private volatile DummyConnection serverToClient = null;
	
	@BeforeEach
	void prep() {
		es = Executors.newCachedThreadPool();
		
		server = new DummyDevice(serverName, serverAddress);
		serverModel = new ServerModel(this.testFolderAddress);
		serverController = new StandardServerController(this.serverModel);
		serverExternal = new DummyServerExternal(serviceID, serviceName, serverController, serverModel, true);
		
		client = new DummyDevice(clientName, clientAddress);
		clientModel = new ClientModel(this.testFolderAddress);
		clientController = new StandardClientController(this.clientModel);
		clientExternal = new DummyClientExternal(serviceID, serviceName, clientController, clientModel);
	
		this.initialDiscovery();
		this.initialDeviceSetup();
		
		es.submit(()->{
			while (!es.isShutdown()) {
				bindConnections();
			}
		});
		
		so1 = o1id+"#20200809235959866#1#0:item1,2;item2,3;";
	}
	
	private void getClientMenuFromServer() {
		while (clientModel.getMenuData().getAllDishMenuItems().length == 0) {
			this.initServerMenu();
			GeneralTestUtilityClass.performWait(100);
		}
	}
	
	private void addPendingSendOrderToClient(String orderID, String serialisedOrder) {
		this.clientModel.addCookingOrder(serialisedOrder);
		this.clientModel.makePendingPaymentOrder(orderID);
		this.clientModel.makePendingSendOrder(orderID, serialisedOrder);
		while (serverModel.getOrder(orderID) == null ||
				!GeneralTestUtilityClass.arrayContains(clientModel.getAllSentOrders(),
						clientModel.getOrderHelper().deserialiseOrderData(serialisedOrder))) {
			GeneralTestUtilityClass.performWait(100);
			this.clientExternal.refreshOrders();
		}
	}
	
	private void bindConnections() {
		while (clientToServer == null || serverToClient == null) {
			clientToServer = (DummyConnection) clientExternal.getConnection(serverAddress);
			serverToClient = (DummyConnection) serverExternal.getConnection(clientAddress);
		}
		InteractionUtilityClass.bindConnectionStreams(clientToServer, serverToClient);
	}
	
	private void initServerMenu() {
		IDishMenu menu = serverModel.getDishMenuHelper().createDishMenu();
		menu.addMenuItem(serverModel.getDishMenuHelper().createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1id));
		menu.addMenuItem(serverModel.getDishMenuHelper().createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2id));
		menu.addMenuItem(serverModel.getDishMenuHelper().createDishMenuItem(i3Name, i3PorSize, i3ProCost, i3Price, i3id));
		serverModel.setDishMenu(serverModel.getDishMenuHelper().dishMenuToData(menu));
	}
	
	private void initialDeviceSetup() {
		this.serverModel.addKnownDevice(clientAddress);
		GeneralTestUtilityClass.performWait(100);
		this.clientModel.addKnownDevice(serverAddress);
		GeneralTestUtilityClass.performWait(100);
	}
	
	private void initialDiscovery() {
		Collection<IDevice> devicesServer = new ArrayList<IDevice>();
		devicesServer.add(client);
		DummyDeviceDiscoveryStrategy dddsServer = new DummyDeviceDiscoveryStrategy();
		dddsServer.setDiscoveredDevices(devicesServer);
		serverExternal.setDiscoveryStrategy(dddsServer);
		serverModel.requestDeviceRediscovery(()->{});
		GeneralTestUtilityClass.performWait(100);
		
		Collection<IDevice> devicesClient = new ArrayList<IDevice>();
		devicesClient.add(server);
		DummyDeviceDiscoveryStrategy dddsClient = new DummyDeviceDiscoveryStrategy();
		dddsClient.setDiscoveredDevices(devicesClient);
		clientExternal.setDiscoveryStrategy(dddsClient);
		clientModel.requestDeviceRediscovery(()->{});
		GeneralTestUtilityClass.performWait(100);
	}
	
	@AfterEach
	void cleanUp() {
		es.shutdownNow();
		
		clientExternal.close();
		clientModel.close();
		
		serverExternal.close();
		serverModel.close();
		
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		GeneralTestUtilityClass.performWait(200);
	}

	@Test
	void deviceFamiliarisingTest() {
		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), clientAddress);
		
		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), serverAddress);
		
		Assertions.assertEquals(serverModel.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceAddress(), clientAddress);
		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
		
		Assertions.assertEquals(clientModel.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceAddress(), serverAddress);
		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
	}
	
	@Test
	void connectionTest() {
//		clientExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
//		serverExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
		
		Assertions.assertEquals(clientModel.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getIsConnected(), true);
		
		Assertions.assertEquals(serverModel.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getIsConnected(), true);
	}
	
	@Test
	void menuExchangeTest() {
		this.initServerMenu();
//		GeneralTestUtilityClass.performWait(100);
//		menuData = clientModel.getMenuData();
		
		IDishMenuData menuData = clientModel.getMenuData();
		while (menuData.getAllDishMenuItems().length == 0) {
			this.initServerMenu();
			GeneralTestUtilityClass.performWait(100);
			menuData = clientModel.getMenuData();
		}
		Assertions.assertTrue(serverModel.getMenuData().equals(menuData));
	}
	
	@Test
	void orderExchangeTest() {
		this.getClientMenuFromServer();
		this.addPendingSendOrderToClient(o1id, so1);
		
		Assertions.assertEquals(serverModel.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(serverModel.getAllUnconfirmedOrders()[0].equals(serverModel.getOrderHelper().deserialiseOrderData(so1)));
		
		Assertions.assertEquals(clientModel.getAllSentOrders().length, 1);
		Assertions.assertTrue(clientModel.getAllSentOrders()[0].equals(clientModel.getOrderHelper().deserialiseOrderData(so1)));
	}
}
