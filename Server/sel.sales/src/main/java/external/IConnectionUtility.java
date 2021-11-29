package external;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;
import external.device.IDevice;

public interface IConnectionUtility {
	Object getServiceID(Object serviceObject);
	String getDeviceName(Object deviceObject);
	String getDeviceAddress(Object deviceObject);
	
	String getServiceURL(IService service);
	String getConnectionTargetAddress(IConnectionObject connObject);
	IConnectionNotifier publishService(IService service);
	IConnectionObject openConnection(String address);
	IConnectionObject acceptAndOpenAlgorithm(IConnectionNotifier notifier);
	
	Collection<IDevice> discoverDevices();
	String getServiceConnectionURL(Object serviceID, IDevice serviceHost);
	
	void closeConnectionObject(IConnectionObject connObject);
	InputStream openInputStream(IConnectionObject connObject);
	OutputStream openOutputStream(IConnectionObject connObject);
	
	IServiceInfo getServiceInfo();
}
