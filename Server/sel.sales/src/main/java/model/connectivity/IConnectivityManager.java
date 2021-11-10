package model.connectivity;

public interface IConnectivityManager {
//	default void addDeviceDatas(IDeviceData[] cds) {
//		if (cds != null) {
//			for (IDeviceData cd : cds) {
//				this.addDeviceData(cd);
//			}
//		}
//	}
//	void addDeviceData(IDeviceData cd);
	
	void addDiscoveredDevice(String deviceName, String deviceAddress);
	void addKnownDevice(String deviceAddress);
	void removeKnownDevice(String deviceAddress);
	void allowKnownDevice(String deviceAddress);
	void blockKnownDevice(String deviceAddress);
	void requestDeviceRediscovery();
	boolean isDeviceRediscoveryRequested();
	boolean isAllowedToConnect(String deviceAddress);
	boolean isConnected(String deviceAddress);
	void deviceConnected(String deviceAddress);
	void deviceDisconnected(String deviceAddress);
	
	int getKnownDeviceCount();
	
	IDeviceData[] getAllKnownDeviceData();
	IDeviceData[] getAllDiscoveredDeviceData();
}
