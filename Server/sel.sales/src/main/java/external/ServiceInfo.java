package external;

import javax.bluetooth.UUID;

public class ServiceInfo {
	private static ServiceInfo instance;
	
	private final UUID serviceID = new UUID(0x1111);
	private final String serviceName = "SEL_Service";
	
	private ServiceInfo() {}
	
	public static ServiceInfo getInstance() {
		if (instance == null) {
			instance = new ServiceInfo();
		}
		return instance;
	}
	
	public UUID getServiceID() {
		return this.serviceID;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}
}
