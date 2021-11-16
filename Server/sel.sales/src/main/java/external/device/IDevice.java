package external.device;

public interface IDevice {
	String getDeviceName();
	String getDeviceAddress();
	Object getDeviceObject();
	boolean equals(Object o);
}
