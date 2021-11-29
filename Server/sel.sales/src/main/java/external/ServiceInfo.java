package external;

public class ServiceInfo implements IServiceInfo {
	private final Object serviceID;
	private final String serviceName;
	
	public ServiceInfo(Object serviceID, String serviceName) {
		this.serviceID = serviceID;
		this.serviceName = serviceName;
	}
	
	@Override
	public Object getServiceID() {
		return this.serviceID;
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}
}
