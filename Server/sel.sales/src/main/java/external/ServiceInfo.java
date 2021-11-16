package external;

public class ServiceInfo {
	private static ServiceInfo instance;
	
	private final String serviceID = "0x1111";
	private final String serviceName = "SEL_Service";
	
	private ServiceInfo() {}
	
	public static ServiceInfo getInstance() {
		if (instance == null) {
			instance = new ServiceInfo();
		}
		return instance;
	}
	
	public String getServiceID() {
		return this.serviceID;
	}
	
	public String getServiceName() {
		return this.serviceName;
	}
}
