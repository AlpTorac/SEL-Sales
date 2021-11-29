package external.device;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import controller.IController;
import model.connectivity.IDeviceData;

public abstract class DeviceManager implements IDeviceManager {

	protected ExecutorService es;
	private static boolean INITIAL_CONNECTION_ALLOWANCE = true;
	private DeviceDiscoveryStrategy cds;
	private Map<String, IDevice> discoveredDevices;
	private Map<String, DeviceManagerEntry> knownDevices;
	
	private DeviceDiscoveryListener cdl;
	
	public DeviceManager(ExecutorService es) {
		this.discoveredDevices = new ConcurrentHashMap<String, IDevice>();
		this.knownDevices = new ConcurrentHashMap<String, DeviceManagerEntry>();
		this.es = es;
	}
	
	public DeviceManager(ExecutorService es, IController controller) {
//		this.discoveredDevices = new ConcurrentHashMap<String, IDevice>();
//		this.knownDevices = new ConcurrentHashMap<String, DeviceManagerEntry>();
//		this.es = es;
//		this.cds = this.initDiscoveryStrategy();
		this(es);
		this.cdl = new DeviceDiscoveryListener(controller);
	}
	
	@Override
	public void setDeviceDiscoveryListener(DeviceDiscoveryListener cdl) {
		this.cdl = cdl;
	}
	
	@Override
	public boolean isAllowedToConnect(String deviceAddress) {
		DeviceManagerEntry e = this.getDeviceEntry(deviceAddress);
		if (e == null) {
			return false;
		}
		return e.isAllowedToConnect();
	}

	@Override
	public void addDevice(String deviceAddress) {
		IDevice Device = this.discoveredDevices.get(deviceAddress);
		if (Device != null) {
			DeviceManagerEntry e = new DeviceManagerEntry(Device, INITIAL_CONNECTION_ALLOWANCE);
			this.knownDevices.put(deviceAddress, e);
		}
	}

	private DeviceManagerEntry getDeviceEntry(String deviceAddress) {
		return this.knownDevices.get(deviceAddress);
	}
	
	@Override
	public IDevice getDevice(String deviceAddress) {
		DeviceManagerEntry wantedDeviceEntry = this.getDeviceEntry(deviceAddress);
		if (wantedDeviceEntry == null) {
			return null;
		}
		return wantedDeviceEntry.getDevice();
	}
	
	@Override
	public void removeDevice(String deviceAddress) {
		this.knownDevices.remove(deviceAddress);
	}

	@Override
	public void allowDevice(String deviceAddress) {
		DeviceManagerEntry e = this.getDeviceEntry(deviceAddress);
		if (e != null) {
			e.setConnectionAllowance(true);
		}
	}

	@Override
	public void blockDevice(String deviceAddress) {
		DeviceManagerEntry e = this.getDeviceEntry(deviceAddress);
		if (e != null) {
			e.setConnectionAllowance(false);
		}
	}
	
	protected void addDiscoveredDevice(IDevice c) {
		this.discoveredDevices.put(c.getDeviceAddress(), c);
	}
	
	protected void discoverDevicesAlgorithmWithAfterAction(Runnable afterAction) {
		this.discoverDevicesAlgorithm();
		afterAction.run();
	}
	
	protected void discoverDevicesAlgorithm() {
		if (this.cds == null) {
			this.cds = this.initDiscoveryStrategy();
		}
		
		Collection<IDevice> dcs = this.cds.discoverDevices();
		
		if (this.discoveredDevices != null) {
			for (IDevice c : dcs) {
//				discoveredDevices.put(c.getDeviceAddress(), c);
				addDiscoveredDevice(c);
				if (this.cdl != null) {
					this.cdl.DeviceDiscovered(c.getDeviceName(), c.getDeviceAddress());
				}
			}
		}
	}
	
	@Override
	public void discoverDevices(Runnable afterAction) {
		es.submit(new Runnable() {
			@Override
			public void run() {
				discoverDevicesAlgorithmWithAfterAction(afterAction);
			}
		});
	}
	
	@Override
	public void setDiscoveryStrategy(DeviceDiscoveryStrategy cds) {
		this.cds = cds;
	}
	
	@Override
	public int getDeviceCount() {
		return this.knownDevices.size();
	}
	
	@Override
	public Collection<IDevice> getDiscoveredDevices() {
		return this.discoveredDevices.values();
	}
	
	@Override
	public void receiveKnownDeviceData(IDeviceData[] deviceData) {
		for (IDeviceData d : deviceData) {
			if (!this.knownDevices.containsKey(d.getDeviceAddress())) {
				this.addDevice(d.getDeviceAddress());
			}
			if (d.getIsAllowedToConnect()) {
				this.allowDevice(d.getDeviceAddress());
			} else {
				this.blockDevice(d.getDeviceAddress());
			}
		}
	}
	
	private class DeviceManagerEntry {
		private IDevice device;
		private boolean isAllowedToConnect;
		
		private DeviceManagerEntry(IDevice device, boolean isAllowedToConnect) {
			this.device = device;
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		private IDevice getDevice() {
			return this.device;
		}
		
		private boolean isAllowedToConnect() {
			return this.isAllowedToConnect;
		}
		
		private void setConnectionAllowance(boolean isAllowedToConnect) {
			this.isAllowedToConnect = isAllowedToConnect;
		}
	}
}
