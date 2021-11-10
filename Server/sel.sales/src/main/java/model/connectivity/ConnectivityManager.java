package model.connectivity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectivityManager implements IConnectivityManager {

	private Map<String, ConnectivityManagerEntry> discoveredDevices;
	private Map<String, ConnectivityManagerEntry> knownDevices;
	
	private boolean rediscover;
	
	public ConnectivityManager() {
		rediscover = true;
		this.discoveredDevices = new ConcurrentHashMap<String, ConnectivityManagerEntry>();
		this.knownDevices = new ConcurrentHashMap<String, ConnectivityManagerEntry>();
	}
	
	@Override
	public void addDiscoveredDevice(String deviceName, String deviceAddress) {
		this.rediscover = false;
		this.discoveredDevices.put(deviceAddress, new ConnectivityManagerEntry(deviceName,deviceAddress,false));
	}

	@Override
	public void addKnownDevice(String deviceAddress) {
		this.knownDevices.put(deviceAddress, new ConnectivityManagerEntry(this.discoveredDevices.get(deviceAddress).getDeviceName(),deviceAddress,true));
	}

	@Override
	public void removeKnownDevice(String deviceAddress) {
		this.knownDevices.remove(deviceAddress);
	}

	@Override
	public void allowKnownDevice(String deviceAddress) {
		this.knownDevices.get(deviceAddress).setConnectionAllowance(true);
	}

	@Override
	public void blockKnownDevice(String deviceAddress) {
		this.knownDevices.get(deviceAddress).setConnectionAllowance(false);
	}

	@Override
	public boolean isAllowedToConnect(String deviceAddress) {
		return this.knownDevices.get(deviceAddress).isAllowedToConnect();
	}

	@Override
	public boolean isConnected(String deviceAddress) {
		return this.knownDevices.get(deviceAddress).isConnected();
	}

	@Override
	public int getKnownDeviceCount() {
		return this.knownDevices.size();
	}
	
	@Override
	public IDeviceData[] getAllKnownDeviceData() {
		return this.knownDevices.values().stream()
		.map(e -> new DeviceData(e.getDeviceName(), e.getDeviceAddress(), e.isAllowedToConnect(), e.isConnected()))
		.toArray(IDeviceData[]::new);
	}
	
	@Override
	public IDeviceData[] getAllDiscoveredDeviceData() {
		return this.discoveredDevices.values().stream()
				.map(e -> new DeviceData(e.getDeviceName(), e.getDeviceAddress(), e.isAllowedToConnect(), e.isConnected()))
				.toArray(IDeviceData[]::new);
	}
	
	@Override
	public void deviceConnected(String deviceAddress) {
		this.knownDevices.get(deviceAddress).setConnectionStatus(true);
	}

	@Override
	public void deviceDisconnected(String deviceAddress) {
		this.knownDevices.get(deviceAddress).setConnectionStatus(false);
	}
	
	@Override
	public boolean isDeviceRediscoveryRequested() {
		return this.rediscover;
	}
	
	@Override
	public void requestDeviceRediscovery() {
		this.rediscover = true;
	}
	
//	@Override
//	public void addDeviceData(IDeviceData cd) {
//		this.addDiscoveredDevice(cd.getDeviceName(), cd.getDeviceAddress());
//		this.addKnownDevice(cd.getDeviceAddress());
//		boolean allowance = cd.getIsAllowedToConnect();
//		if (allowance) {
//			this.allowKnownDevice(cd.getDeviceAddress());
//		} else {
//			this.blockKnownDevice(cd.getDeviceAddress());
//		}
////		this.discoveredDevices.put(cd.getDeviceAddress(), new ConnectivityManagerEntry(
////				cd.getDeviceName(),
////				cd.getDeviceAddress(),
////				cd.getIsAllowedToConnect()));
////		this.knownDevices.put(cd.getDeviceAddress(), new ConnectivityManagerEntry(
////				cd.getDeviceName(),
////				cd.getDeviceAddress(),
////				cd.getIsAllowedToConnect()));
//	}
	
	private class ConnectivityManagerEntry {
		private String deviceName;
		private String deviceAddress;
		private boolean isAllowedToConnect;
		private boolean isConnected;
		
		private ConnectivityManagerEntry(String deviceName, String deviceAddress, boolean isAllowedToConnect) {
			this.deviceName = deviceName;
			this.deviceAddress = deviceAddress;
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		public String getDeviceName() {
			return this.deviceName;
		}
		
		public String getDeviceAddress() {
			return this.deviceAddress;
		}
		
		public boolean isAllowedToConnect() {
			return this.isAllowedToConnect;
		}
		
		public boolean isConnected() {
			return this.isConnected;
		}
		
		public void setConnectionAllowance(boolean isAllowedToConnect) {
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		public void setConnectionStatus(boolean isConnected) {
			this.isConnected = isConnected;
		}
	}
}
