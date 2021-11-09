package model.connectivity;

public class DeviceData implements IDeviceData {
	
	private String deviceName;
	private String deviceAddress;
	private boolean isAllowedToConnect;
	private boolean isConnected;
	
	public DeviceData(String deviceName, String deviceAddress, boolean isAllowedToConnect, boolean isConnected) {
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.isAllowedToConnect = isAllowedToConnect;
		this.isConnected = isConnected;
	}
	
	public DeviceData(String deviceName, String deviceAddress, boolean isAllowedToConnect) {
		this.deviceName = deviceName;
		this.deviceAddress = deviceAddress;
		this.isAllowedToConnect = isAllowedToConnect;
		this.isConnected = false;
	}
	
	@Override
	public String getDeviceName() {
		return this.deviceName;
	}

	@Override
	public String getDeviceAddress() {
		return this.deviceAddress;
	}

	@Override
	public boolean getIsAllowedToConnect() {
		return this.isAllowedToConnect;
	}

	@Override
	public boolean getIsConnected() {
		return this.isConnected;
	}
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDeviceData)) {
			return false;
		}
		IDeviceData castedO = (IDeviceData) o;
		return this.getDeviceName().equals(castedO.getDeviceName()) &&
				this.getDeviceAddress().equals(castedO.getDeviceAddress()) &&
				this.getIsConnected() == castedO.getIsConnected() && 
				this.getIsAllowedToConnect() == castedO.getIsAllowedToConnect();
	}
}
