package test.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.GeneralEvent;
import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import model.order.OrderData;
import server.controller.IServerController;
import server.model.IServerModel;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;

class ConnectivityIntegrationTest extends FXTestTemplate {

	private IServerModel model;
	private IServerController controller;
	
	private IDeviceData discoveredDevice1Data;
	private IDeviceData discoveredDevice2Data;
	
	private IDeviceData knownDevice1Data;
	private IDeviceData knownDevice2Data;
	
	@BeforeEach
	void prep() {
		initSetup();
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
		this.closeAll(model);
	}
	
	private void initSetup() {
		model = this.initServerModel();
		controller = this.initServerController(model);
		
		this.addDishMenuToServerModel(model);
		this.initOrders(model);
	}
	
	@Test
	void addDiscoveredDeviceTest() {
		initSetup();
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
		initSetup();
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
		initSetup();
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
		
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {oData1});
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {oData2});
		controller.handleApplicationEvent(GeneralEvent.ADD_ORDER, new Object[] {oData3});
		
		Assertions.assertEquals(3, model.getAllUnconfirmedOrders().length);
		Assertions.assertEquals(0, model.getAllConfirmedOrders().length);
		
//		OrderData[] orders = model.getOrderHelper().deserialiseOrderDatas(
//				serialisedOrder1 + System.lineSeparator() + 
//				serialisedOrder2 + System.lineSeparator() +
//				serialisedOrder3 + System.lineSeparator()
//				);
		
		OrderData[] orders = new OrderData[3];
		
		orders[0] = oData1;
		orders[1] = oData2;
		orders[2] = oData3;
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(model.getAllUnconfirmedOrders(), orders,
				(od1,od2)->this.ordersEqual(od1, od2)));
	}
}
