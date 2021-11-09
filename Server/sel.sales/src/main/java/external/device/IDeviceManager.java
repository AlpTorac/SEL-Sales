package external.device;

import java.util.Collection;

import model.connectivity.IDeviceData;

public interface IDeviceManager {
	void discoverDevices(Runnable afterAction);
	void addDevice(String deviceAddress);
	void removeDevice(String deviceAddress);
	void allowDevice(String deviceAddress);
	void blockDevice(String deviceAddress);
	boolean isAllowedToConnect(String deviceAddress);
	Collection<IDevice> getDiscoveredDevices();
	void receiveKnownDeviceData(IDeviceData[] deviceData);
	IDevice getDevice(String deviceAddress);
	DeviceDiscoveryStrategy initDiscoveryStrategy();
	void setDiscoveryStrategy(DeviceDiscoveryStrategy cds);
	void setDeviceDiscoveryListener(DeviceDiscoveryListener cdl);
	int getDeviceCount();
}
