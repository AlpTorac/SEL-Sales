package test.external.device;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.device.IDevice;
import test.external.dummy.DummyDevice;
@Execution(value = ExecutionMode.SAME_THREAD)
class DeviceTest {
	private String Device1Name;
	private String Device1Address;
	private String Device2Name;
	private String Device2Address;
	private IDevice Device1;
	private IDevice Device2;
	
	@BeforeEach
	void prep() {
		this.Device1Name = "Device1";
		this.Device1Address = "Device1Address";
		this.Device1Name = "Device2";
		this.Device1Address = "Device2Address";
		this.Device1 = new DummyDevice(this.Device1Name, this.Device1Address);
		this.Device2 = new DummyDevice(this.Device2Name, this.Device2Address);
	}
	
	@Test
	void getDeviceNameTest() {
		Assertions.assertEquals(this.Device1Name, Device1.getDeviceName());
	}

	@Test
	void getDeviceAddressTest() {
		Assertions.assertEquals(this.Device1Address, Device1.getDeviceAddress());
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void equalsTest() {
		Assertions.assertFalse(Device1.equals(null));
		Assertions.assertFalse(Device1.equals(Integer.valueOf(12)));
		Assertions.assertFalse(Device1.equals(Device2));
		
		Assertions.assertTrue(Device1.equals(new DummyDevice(Device1Name, Device1Address)));
		Assertions.assertTrue(Device1.equals(Device1));
		Assertions.assertTrue(Device1.equals(new DummyDevice(Device2Name, Device1Address)));
	}
}
