package test.model.connectivity;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.ExternalUpdatable;
import model.Updatable;
import model.connectivity.DeviceData;
import model.connectivity.FileDeviceDataSerialiser;
import model.connectivity.IDeviceData;
import server.model.IServerModel;
import server.model.ServerModel;
import model.connectivity.IConnectivityManager;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyExternal;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityManagerTest {

	private IConnectivityManager connManager;
	private IServerModel model;
	private DummyExternal external;
	private FileDeviceDataSerialiser deviceDataSerialiser = new FileDeviceDataSerialiser();
	
	private IDeviceData discoveredDevice1Data;
	private IDeviceData discoveredDevice2Data;
	
	private IDeviceData knownDevice1Data;
	private IDeviceData knownDevice2Data;
	
	private String device1Name;
	private String device1Address;
	private String device2Name;
	private String device2Address;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	@BeforeEach
	void prep() {
		device1Name = "c1n";
		device1Address = "c1a";
		device2Name = "c2n";
		device2Address = "c2a";
		discoveredDevice1Data = new DeviceData(device1Name,device1Address,false,false);
		discoveredDevice2Data = new DeviceData(device2Name,device2Address,false,false);
		knownDevice1Data = new DeviceData(device1Name,device1Address,true,false);
		knownDevice2Data = new DeviceData(device2Name,device2Address,false,false);
		initConnManager();
		connManager.addDiscoveredDevice(device1Name, device1Address);
		connManager.addDiscoveredDevice(device2Name, device2Address);
		connManager.addKnownDevice(device1Address);
		connManager.addKnownDevice(device2Address);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	private void initConnManager() {
		model = new ServerModel(this.testFolderAddress);
		Updatable u = new ExternalUpdatable() {

			@Override
			public void subscribe() {
				model.subscribe(this);
			}

			@Override
			public void rediscoverDevices(Runnable afterDiscoveryAction) {
				afterDiscoveryAction.run();
			}
			
		};
		u.subscribe();
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((ServerModel) model, "connManager");
	}
	
	@Test
	void setKnownDevicesTest() {
		model.close();
		this.initConnManager();
		connManager.addDiscoveredDevice(device1Name, device1Address);
		connManager.addDiscoveredDevice(device2Name, device2Address);
		IDeviceData[] deviceDatas = new IDeviceData[] {
				knownDevice1Data,
				knownDevice2Data
			};
		this.model.setKnownDevices(this.deviceDataSerialiser.serialiseDeviceDatas(deviceDatas));
		GeneralTestUtilityClass.arrayContentEquals(this.model.getAllKnownDeviceData(), deviceDatas);
	}
	
	@Test
	void setKnownDevicesNullDataTest() {
		model.close();
		this.initConnManager();
		IDeviceData[] deviceDatas = new IDeviceData[0];
		this.model.setKnownDevices(this.deviceDataSerialiser.serialiseDeviceDatas(deviceDatas));
		GeneralTestUtilityClass.arrayContentEquals(this.model.getAllKnownDeviceData(), deviceDatas);
	}
	
	@Test
	void addDiscoveredDeviceTest() {
		initConnManager();
		connManager.addDiscoveredDevice(device1Name, device1Address);
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
		connManager.addDiscoveredDevice(device1Name, device1Address);
		connManager.addDiscoveredDevice(device2Name, device2Address);
		connManager.addKnownDevice(device1Address);
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
		connManager.removeKnownDevice(device1Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice1Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownDeviceData(), discoveredDevice2Data,
				(kc, dc) -> {
					return kc.getDeviceName().equals(dc.getDeviceName()) && kc.getDeviceAddress().equals(dc.getDeviceAddress());
				}));
		connManager.removeKnownDevice(device2Address);
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
		Assertions.assertTrue(connManager.isAllowedToConnect(device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(device2Address));
		connManager.blockKnownDevice(device1Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(device2Address));
		connManager.blockKnownDevice(device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(device2Address));
	}
	
	@Test
	void allowDeviceTest() {
		connManager.blockKnownDevice(device1Address);
		connManager.blockKnownDevice(device2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(device2Address));
		
		connManager.allowKnownDevice(device1Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(device1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(device2Address));
		
		connManager.allowKnownDevice(device2Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(device1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(device2Address));
	}
	
	@Test
	void requestRediscoveryTest() {
		connManager.requestDeviceRediscovery();
		Assertions.assertTrue(connManager.isDeviceRediscoveryRequested());
	}
	
	@Test
	void isConnectedTest() {
		Assertions.assertFalse(connManager.isConnected(device1Address));
		Assertions.assertFalse(connManager.isConnected(device2Address));
		
		connManager.deviceConnected(device1Address);
		Assertions.assertTrue(connManager.isConnected(device1Address));
		Assertions.assertFalse(connManager.isConnected(device2Address));
		
		connManager.deviceConnected(device2Address);
		Assertions.assertTrue(connManager.isConnected(device1Address));
		Assertions.assertTrue(connManager.isConnected(device2Address));
		
		connManager.deviceDisconnected(device1Address);
		Assertions.assertFalse(connManager.isConnected(device1Address));
		Assertions.assertTrue(connManager.isConnected(device2Address));
		
		connManager.deviceDisconnected(device2Address);
		Assertions.assertFalse(connManager.isConnected(device1Address));
		Assertions.assertFalse(connManager.isConnected(device2Address));
	}
	
	@Test
	void getKnownDeviceCountTest() {
		Assertions.assertEquals(2, connManager.getKnownDeviceCount());
		connManager.removeKnownDevice(device1Address);
		Assertions.assertEquals(1, connManager.getKnownDeviceCount());
		connManager.removeKnownDevice(device2Address);
		Assertions.assertEquals(0, connManager.getKnownDeviceCount());
	}
	
	@Test
	void getDeviceDataTest() {
		initConnManager();
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 0);
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addDiscoveredDevice(device1Name, device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addDiscoveredDevice(device2Name, device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 0);
		connManager.addKnownDevice(device1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 1);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		connManager.addKnownDevice(device2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[0].equals(discoveredDevice1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredDeviceData()[1].equals(discoveredDevice2Data));
		Assertions.assertEquals(connManager.getAllKnownDeviceData().length, 2);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[0].equals(knownDevice1Data));
		connManager.blockKnownDevice(device2Address);
		Assertions.assertTrue(connManager.getAllKnownDeviceData()[1].equals(knownDevice2Data));
	}
}
