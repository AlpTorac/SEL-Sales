package test.external.device;

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

import external.device.IDevice;
import external.device.IDeviceManager;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyDeviceDiscoveryStrategy;
import test.external.dummy.DummyDeviceManager;
@Execution(value = ExecutionMode.SAME_THREAD)
class DeviceManagerTest {
	private String Device1Name;
	private String Device1Address;
	private String Device2Name;
	private String Device2Address;
	private IDevice Device1;
	private IDevice Device2;
	private IDeviceManager manager;
	private ExecutorService es;
	
	@BeforeEach
	void prep() {
		this.es = Executors.newCachedThreadPool();
		this.Device1Name = "Device1";
		this.Device1Address = "Device1Address";
		this.Device2Name = "Device2";
		this.Device2Address = "Device2Address";
		this.Device1 = new DummyDevice(this.Device1Name, this.Device1Address);
		this.Device2 = new DummyDevice(this.Device2Name, this.Device2Address);
		this.manager = new DummyDeviceManager(es);
		this.discoverDevices();
		manager.addDevice(Device1Address);
		manager.addDevice(Device2Address);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			this.es.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void discoverDevices() {
		DummyDeviceDiscoveryStrategy cds = new DummyDeviceDiscoveryStrategy();
		Collection<IDevice> cs = new ArrayList<IDevice>();
		cs.add(Device1);
		cs.add(Device2);
		cds.setDiscoveredDevices(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverDevices(()->{});
		GeneralTestUtilityClass.performWait(300);
	}
	
	@Test
	void getDeviceTest() {
		Assertions.assertTrue(Device1.equals(manager.getDevice(Device1.getDeviceAddress())));
		Assertions.assertTrue(Device2.equals(manager.getDevice(Device2.getDeviceAddress())));
		Assertions.assertNull(manager.getDevice("fkjhgsdfhggsdfkhg"));
	}
	
	@Test
	void addDeviceTest() {
		this.manager = new DummyDeviceManager(es);
		this.discoverDevices();
		Assertions.assertEquals(0, manager.getDeviceCount());
		manager.addDevice(Device1Address);
		Assertions.assertEquals(1, manager.getDeviceCount());
		Assertions.assertTrue(Device1.equals(manager.getDevice(Device1.getDeviceAddress())));
		
		Assertions.assertEquals(1, manager.getDeviceCount());
		manager.addDevice(Device2Address);
		Assertions.assertEquals(2, manager.getDeviceCount());
		Assertions.assertTrue(Device1.equals(manager.getDevice(Device1.getDeviceAddress())));
		Assertions.assertTrue(Device2.equals(manager.getDevice(Device2.getDeviceAddress())));
	}
	
	@Test
	void blockDeviceTest() {
		Assertions.assertTrue(manager.isAllowedToConnect(Device1Address));
		manager.blockDevice(Device1Address);
		Assertions.assertFalse(manager.isAllowedToConnect(Device1Address));
	}
	
	@Test
	void allowDeviceTest() {
		Assertions.assertTrue(manager.isAllowedToConnect(Device1Address));
		manager.blockDevice(Device1Address);
		Assertions.assertFalse(manager.isAllowedToConnect(Device1Address));
		manager.allowDevice(Device1Address);
		Assertions.assertTrue(manager.isAllowedToConnect(Device1Address));
	}
	
	@Test
	void removeDeviceTest() {
		Assertions.assertEquals(2, manager.getDeviceCount());
		manager.removeDevice(Device1Address);
		Assertions.assertEquals(1, manager.getDeviceCount());
		
		manager.removeDevice("gflkjhgdfshkjgsdfkjlh");
		
		Assertions.assertEquals(1, manager.getDeviceCount());
		manager.removeDevice(Device2Address);
		Assertions.assertEquals(0, manager.getDeviceCount());
	}
	
	@Test
	void discoverDevicesTest() {
		this.manager = new DummyDeviceManager(es);
		Collection<IDevice> DeviceCol = new ArrayList<IDevice>();
		DeviceCol.add(Device1);
		DeviceCol.add(Device2);
		DummyDeviceDiscoveryStrategy cds = new DummyDeviceDiscoveryStrategy();
		cds.setDiscoveredDevices(DeviceCol);
		manager.setDiscoveryStrategy(cds);
		manager.discoverDevices(()->{});
		GeneralTestUtilityClass.performWait(300);
		Assertions.assertTrue(manager.getDiscoveredDevices().containsAll(DeviceCol));
		Assertions.assertFalse(manager.isAllowedToConnect(Device1Address));
		Assertions.assertFalse(manager.isAllowedToConnect(Device2Address));
	}
}
