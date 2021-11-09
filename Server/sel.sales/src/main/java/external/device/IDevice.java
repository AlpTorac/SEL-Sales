package external.device;

public interface IDevice {
	String getDeviceName();
	String getDeviceAddress();
	boolean equals(Object o);
}
