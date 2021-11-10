package test.controller;

import java.io.File;
import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GeneralEvent;
import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import model.connectivity.IConnectivityManager;
import model.order.IOrderData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import test.GeneralTestUtilityClass;

class ConnectivityIntegrationTest {

	private IServerModel model;
	private IServerController controller;
	private IConnectivityManager connManager;
	
	private IDeviceData discoveredDevice1Data;
	private IDeviceData discoveredDevice2Data;
	
	private IDeviceData knownDevice1Data;
	private IDeviceData knownDevice2Data;
	
	private String Device1Name;
	private String Device1Address;
	private String Device2Name;
	private String Device2Address;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private String i3id = "item3";
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		init();
		Device1Name = "c1n";
		Device1Address = "c1a";
		Device2Name = "c2n";
		Device2Address = "c2a";
		discoveredDevice1Data = new DeviceData(Device1Name,Device1Address,false,false);
		discoveredDevice2Data = new DeviceData(Device2Name,Device2Address,false,false);
		knownDevice1Data = new DeviceData(Device1Name,Device1Address,true,false);
		knownDevice2Data = new DeviceData(Device2Name,Device2Address,true,false);
		controller.getModel().addDiscoveredDevice(Device1Name, Device1Address);
		controller.getModel().addDiscoveredDevice(Device2Name, Device2Address);
		controller.getModel().addKnownDevice(Device1Address);
		controller.getModel().addKnownDevice(Device2Address);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	private void initConnManager() {
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((ServerModel) model, "connManager");
	}
	
	private void init() {
		model = new ServerModel(this.testFolderAddress);
		controller = new StandardServerController(model);
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
		initConnManager();
	}
	
	@Test
	void addDiscoveredDeviceTest() {
		init();
		controller.getModel().addDiscoveredDevice(Device1Name, Device1Address);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllDiscoveredDeviceData(), discoveredDevice1Data,
				(dc1, dc2) -> {
					return dc1.getDeviceName().equals(dc2.getDeviceName()) && dc1.getDeviceAddress().equals(dc2.getDeviceAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllDiscoveredDeviceData(), discoveredDevice2Data,
				(dc1, dc2) -> {
					return dc1.getDeviceName().equals(dc2.getDeviceName()) && dc1.getDeviceAddress().equals(dc2.getDeviceAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
	}
	
	@Test
	void addKnownDeviceTest() {
		init();
		controller.getModel().addDiscoveredDevice(Device1Name, Device1Address);
		controller.getModel().addDiscoveredDevice(Device2Name, Device2Address);
		controller.getModel().addKnownDevice(Device1Address);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
	}
	
	@Test
	void removeKnownDeviceTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		controller.getModel().removeKnownDevice(Device1Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		controller.getModel().removeKnownDevice(Device2Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
	}
	
	@Test
	void blockDeviceTest() {
		Assertions.assertTrue(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(Device2Address));
		controller.getModel().blockKnownDevice(Device1Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(Device2Address));
		controller.getModel().blockKnownDevice(Device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
	}
	
	@Test
	void allowDeviceTest() {
		controller.getModel().blockKnownDevice(Device1Address);
		controller.getModel().blockKnownDevice(Device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
		
		controller.getModel().allowKnownDevice(Device1Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
		
		controller.getModel().allowKnownDevice(Device2Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(Device2Address));
	}
	
	@Test
	void requestRediscoveryTest() {
		connManager.requestDeviceRediscovery();
		Assertions.assertTrue(connManager.isDeviceRediscoveryRequested());
	}
	
	@Test
	void isConnectedTest() {
		Assertions.assertFalse(connManager.isConnected(Device1Address));
		Assertions.assertFalse(connManager.isConnected(Device2Address));
		
		controller.getModel().deviceConnected(Device1Address);
		Assertions.assertTrue(connManager.isConnected(Device1Address));
		Assertions.assertFalse(connManager.isConnected(Device2Address));
		
		controller.getModel().deviceConnected(Device2Address);
		Assertions.assertTrue(connManager.isConnected(Device1Address));
		Assertions.assertTrue(connManager.isConnected(Device2Address));
		
		controller.getModel().deviceDisconnected(Device1Address);
		Assertions.assertFalse(connManager.isConnected(Device1Address));
		Assertions.assertTrue(connManager.isConnected(Device2Address));
		
		controller.getModel().deviceDisconnected(Device2Address);
		Assertions.assertFalse(connManager.isConnected(Device1Address));
		Assertions.assertFalse(connManager.isConnected(Device2Address));
	}
	
	@Test
	void getKnownDeviceCountTest() {
		Assertions.assertEquals(2, connManager.getKnownDeviceCount());
		controller.getModel().removeKnownDevice(Device1Address);
		Assertions.assertEquals(1, connManager.getKnownDeviceCount());
		controller.getModel().removeKnownDevice(Device2Address);
		Assertions.assertEquals(0, connManager.getKnownDeviceCount());
	}
	
	@Test
	void getDeviceDataTest() {
		init();
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 0);
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		controller.getModel().addDiscoveredDevice(Device1Name, Device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		controller.getModel().addDiscoveredDevice(Device2Name, Device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		controller.getModel().addKnownDevice(Device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		controller.getModel().addKnownDevice(Device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[1].equals(knownDevice2Data));
	}
	
	@Test
	void addOrdersViaController() {
		Assertions.assertEquals(0, model.getAllUnconfirmedOrders().length);
		Assertions.assertEquals(0, model.getAllConfirmedOrders().length);
		
		String serialisedOrder1 = "order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1;";
		String serialisedOrder2 = "order6#20200813000000183#1#1:item3,5;item3,4;";
		String serialisedOrder3 = "order7#20200909112233937#0#0:item1,2;item2,5;";
		
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {serialisedOrder1});
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {serialisedOrder2});
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {serialisedOrder3});
		
		Assertions.assertEquals(3, model.getAllUnconfirmedOrders().length);
		Assertions.assertEquals(0, model.getAllConfirmedOrders().length);
		
		IOrderData[] orders = model.getOrderHelper().deserialiseOrderDatas(
				serialisedOrder1 + System.lineSeparator() + 
				serialisedOrder2 + System.lineSeparator() +
				serialisedOrder3 + System.lineSeparator()
				);
		
		GeneralTestUtilityClass.arrayContentEquals(model.getAllUnconfirmedOrders(), orders);
	}
}
