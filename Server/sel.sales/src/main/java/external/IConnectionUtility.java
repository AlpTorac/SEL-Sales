package external;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;
import external.device.IDevice;

public interface IConnectionUtility extends Closeable {
//	Object getServiceID(Object serviceObject);
//	String getDeviceName(Object deviceObject);
//	String getDeviceAddress(Object deviceObject);
	
	/**
	 * Makes the necessary preparations to start networking (such as initialising the bluetooth stack)
	 */
	void init();
	/**
	 * Turns on the constructs initialised in {@link #init()}
	 */
	void start();
	/**
	 * Closes the constructs used in {@link #init()} and {@link #start()}
	 */
	void close();
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
