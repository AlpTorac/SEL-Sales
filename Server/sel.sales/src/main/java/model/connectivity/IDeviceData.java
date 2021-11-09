package model.connectivity;

public interface IDeviceData {
	String getDeviceName();
	String getDeviceAddress();
	boolean getIsAllowedToConnect();
	boolean getIsConnected();
	boolean equals(Object o);
}
