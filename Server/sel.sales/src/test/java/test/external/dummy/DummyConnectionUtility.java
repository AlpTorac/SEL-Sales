package test.external.dummy;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import external.IConnectionUtility;
import external.IServiceInfo;
import external.ServiceInfo;
import external.connection.ConnectionObject;
import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;
import external.device.IDevice;
import external.standard.StandardConnectionNotifier;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection.ISwithBuffer;
import test.external.dummy.DummyConnection.OSwithBuffer;

public class DummyConnectionUtility implements IConnectionUtility {
	private DummyDeviceDiscoveryStrategy ddds;
	private Map<IConnectionObject, DummyConnection> coToStreams = new ConcurrentHashMap<IConnectionObject, DummyConnection>();
	
	private ServiceInfo currentServiceInfo = new ServiceInfo("serviceID", "serviceName");
	
	private volatile DummyConnectionUtility connectionTarget;
	private volatile String currentAddress;

	private String ownerAddress;
	
	public DummyConnectionUtility(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	
	public String getOwnerAddress() {
		return this.ownerAddress;
	}
	
	public void setConnectionTarget(DummyConnectionUtility connUtil) {
		this.connectionTarget = connUtil;
	}
	
	public String getCurrentAddress() {
		return this.currentAddress;
	}
	
	public void setCurrentAddress(String address) {
		while (this.getCurrentAddress() != null) {
			
		}
		this.currentAddress = address;
	}
	
	public void requestToConnect() {
		System.out.println("Requesting to connect");
		while (this.connectionTarget == null) {
			
		}
		while (this.connectionTarget.getConnection(this.getOwnerAddress()) == null) {
			System.out.println("Waiting on the server to connect to client");
			this.connectionTarget.setCurrentAddress(this.getOwnerAddress());
		}
	}
	
	@Override
	public String getServiceURL(IService service) {
		return "URL of service " + service.getName();
	}

	@Override
	public String getConnectionTargetAddress(IConnectionObject connObject) {
		return (String) connObject.getConnectionObject();
//		return this.coToStreams.get(connObject).getTargetDeviceAddress();
	}

	@Override
	public IConnectionNotifier publishService(IService service) {
		return new StandardConnectionNotifier(service, this);
	}

	@Override
	public IConnectionObject openConnection(String address) {
		System.out.println("Opening connection to: " + address);
		IConnectionObject co = null;
		if (address != null) {
			co = new ConnectionObject(this, address);
			this.putToMap(co);
			this.requestToConnect();
		}
		return co;
	}

	@Override
	public IConnectionObject acceptAndOpenAlgorithm(IConnectionNotifier notifier) {
		while (this.currentAddress == null) {
//		System.out.println("Waiting for an incoming connection, current: " + this.currentAddress);
			GeneralTestUtilityClass.performWait(100);
		}
		IConnectionObject co = null;
//		if (this.currentAddress != null) {
//			co = new ConnectionObject(this, this.currentAddress);
//			this.putToMap(co);
//			this.currentAddress = null;
//		}
		co = new ConnectionObject(this, this.currentAddress);
		this.putToMap(co);
		System.out.println("Accept and open algorithm used for: " + this.currentAddress);
		this.currentAddress = null;
		return co;
	}

	@Override
	public Collection<IDevice> discoverDevices() {
		return this.getDeviceDiscoveryStrategy().discoverDevices();
	}
	
	/**
	 * !!! Returns the service host's address instead !!!
	 */
	@Override
	public String getServiceConnectionURL(Object serviceID, IDevice serviceHost) {
		return serviceHost.getDeviceAddress();
	}
	
	@Override
	public void closeConnectionObject(IConnectionObject connObject) {
		if (this.coToStreams.containsKey(connObject)) {
			try {
				this.coToStreams.get(connObject).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void putToMap(IConnectionObject connObject) {
		if (!this.coToStreams.values().stream().anyMatch(dc -> dc.getTargetDeviceAddress().equals(connObject.getTargetAddress()))) {
			System.out.println("Connection added to map: " + connObject.getTargetAddress());
			this.coToStreams.put(connObject, new DummyConnection(connObject.getTargetAddress()));
		}
	}
	
	@Override
	public ISwithBuffer openInputStream(IConnectionObject connObject) {
		if (!this.coToStreams.containsKey(connObject)) {
			this.putToMap(connObject);
		}
		return this.coToStreams.get(connObject).getInputStream();
	}

	@Override
	public OSwithBuffer openOutputStream(IConnectionObject connObject) {
		if (!this.coToStreams.containsKey(connObject)) {
			this.putToMap(connObject);
		}
		return this.coToStreams.get(connObject).getOutputStream();
	}

	@Override
	public IServiceInfo getServiceInfo() {
		return this.currentServiceInfo;
	}

	public void setServiceInfo(Object serviceID, String serviceName) {
		this.currentServiceInfo = new ServiceInfo(serviceID, serviceName);
	}
	
	public void setDeviceDiscoveryStrategy(DummyDeviceDiscoveryStrategy ddds) {
		this.ddds = ddds;
	}
	public DummyDeviceDiscoveryStrategy getDeviceDiscoveryStrategy() {
		return this.ddds;
	}
	
	public DummyConnection getConnection(String deviceAddress) {
		Optional<DummyConnection> o = this.coToStreams.values().stream().filter(dc -> dc.getTargetDeviceAddress().equals(deviceAddress)).findFirst();
		return o.isPresent() ? o.get() : null;
	}
}
