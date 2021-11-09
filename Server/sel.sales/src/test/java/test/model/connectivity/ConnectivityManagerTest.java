package test.model.connectivity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import server.model.IServerModel;
import server.model.ServerModel;
import model.connectivity.IConnectivityManager;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityManagerTest {

	private IConnectivityManager connManager;
	private IServerModel model;
	
	private IDeviceData discoveredDevice1Data;
	private IDeviceData discoveredDevice2Data;
	
	private IDeviceData knownDevice1Data;
	private IDeviceData knownDevice2Data;
	
	private String Device1Name;
	private String Device1Address;
	private String Device2Name;
	private String Device2Address;
	
	@BeforeEach
	void prep() {
		Device1Name = "c1n";
		Device1Address = "c1a";
		Device2Name = "c2n";
		Device2Address = "c2a";
		discoveredDevice1Data = new DeviceData(Device1Name,Device1Address,false,false);
		discoveredDevice2Data = new DeviceData(Device2Name,Device2Address,false,false);
		knownDevice1Data = new DeviceData(Device1Name,Device1Address,true,false);
		knownDevice2Data = new DeviceData(Device2Name,Device2Address,true,false);
		initConnManager();
		connManager.addDiscoveredDevice(Device1Name, Device1Address);
		connManager.addDiscoveredDevice(Device2Name, Device2Address);
		connManager.addKnownDevice(Device1Address);
		connManager.addKnownDevice(Device2Address);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	private void initConnManager() {
		model = new ServerModel();
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((ServerModel) model, "connManager");
	}
	
	@Test
	void addDiscoveredDeviceTest() {
		initConnManager();
		connManager.addDiscoveredDevice(Device1Name, Device1Address);
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
		initConnManager();
		connManager.addDiscoveredDevice(Device1Name, Device1Address);
		connManager.addDiscoveredDevice(Device2Name, Device2Address);
		connManager.addKnownDevice(Device1Address);
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
		connManager.removeKnownDevice(Device1Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		connManager.removeKnownDevice(Device2Address);
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
		connManager.blockKnownDevice(Device1Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(Device2Address));
		connManager.blockKnownDevice(Device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
	}
	
	@Test
	void allowDeviceTest() {
		connManager.blockKnownDevice(Device1Address);
		connManager.blockKnownDevice(Device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
		
		connManager.allowKnownDevice(Device1Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(Device2Address));
		
		connManager.allowKnownDevice(Device2Address);
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
		
		connManager.DeviceConnected(Device1Address);
		Assertions.assertTrue(connManager.isConnected(Device1Address));
		Assertions.assertFalse(connManager.isConnected(Device2Address));
		
		connManager.DeviceConnected(Device2Address);
		Assertions.assertTrue(connManager.isConnected(Device1Address));
		Assertions.assertTrue(connManager.isConnected(Device2Address));
		
		connManager.DeviceDisconnected(Device1Address);
		Assertions.assertFalse(connManager.isConnected(Device1Address));
		Assertions.assertTrue(connManager.isConnected(Device2Address));
		
		connManager.DeviceDisconnected(Device2Address);
		Assertions.assertFalse(connManager.isConnected(Device1Address));
		Assertions.assertFalse(connManager.isConnected(Device2Address));
	}
	
	@Test
	void getKnownDeviceCountTest() {
		Assertions.assertEquals(2, connManager.getKnownDeviceCount());
		connManager.removeKnownDevice(Device1Address);
		Assertions.assertEquals(1, connManager.getKnownDeviceCount());
		connManager.removeKnownDevice(Device2Address);
		Assertions.assertEquals(0, connManager.getKnownDeviceCount());
	}
	
	@Test
	void getDeviceDataTest() {
		initConnManager();
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 0);
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addDiscoveredDevice(Device1Name, Device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addDiscoveredDevice(Device2Name, Device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addKnownDevice(Device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		connManager.addKnownDevice(Device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[1].equals(knownDevice2Data));
	}
}
