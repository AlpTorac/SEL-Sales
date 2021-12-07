package external;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import external.connection.ConnectionObject;
import external.connection.IConnectionObject;
import external.connection.IService;
import external.connection.incoming.IConnectionNotifier;
import external.device.IDevice;
import external.standard.StandardConnectionNotifier;
import external.standard.StandardDevice;

public class WindowsBluetoothConnectionUtility implements IConnectionUtility {
	private final UUID serviceID = new UUID(0x1111);
	private final String serviceName = "SEL_Service";
	
//	private final int serviceIDEntryID = 0x0003;
//	
//	@Override
//	public UUID getServiceID(Object serviceObject) {
//		return (UUID) ((ServiceRecord) serviceObject).getAttributeValue(serviceIDEntryID).getValue();
//	}
//
//	@Override
//	public String getDeviceName(Object deviceObject) {
//		String result = null;
//		RemoteDevice dev = (RemoteDevice) deviceObject;
//		try {
//			result = dev.getFriendlyName(false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//	@Override
//	public String getDeviceAddress(Object deviceObject) {
//		return ((RemoteDevice) deviceObject).getBluetoothAddress();
//	}

	@Override
	public IConnectionNotifier publishService(IService service) {
		try {
			Object o = Connector.open(service.getURL());
			return new StandardConnectionNotifier(service, this, o);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IConnectionObject openConnection(String address) {
		try {
			Object o = Connector.open(address);
			return new ConnectionObject(this, o);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IConnectionObject acceptAndOpenAlgorithm(IConnectionNotifier notifier) {
		try {
			StreamConnection o = ((StreamConnectionNotifier) notifier.getConnectionNotifierObject()).acceptAndOpen();
			System.out.println("Opened connection for: " + notifier.getService().getURL());
			return new ConnectionObject(this, o);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Collection<IDevice> discoverDevices() {
		Collection<IDevice> devices = new CopyOnWriteArrayList<IDevice>();
		LocalDevice lDev = null;
		try {
			lDev = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
//			e.printStackTrace();
		}
		
		final Object lock = new Object();
		
		DiscoveryListener l = new DiscoveryListener() {

			@Override
			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				String name = null, address;
				try {
					name = btDevice.getFriendlyName(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
				address = btDevice.getBluetoothAddress();
				devices.add(new StandardDevice(name, address, btDevice));
			}

			@Override
			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void serviceSearchCompleted(int transID, int respCode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inquiryCompleted(int discType) {
				synchronized (lock) {
					lock.notifyAll();
				}
			}
			
		};
		boolean started = false;
		try {
			started = lDev.getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, l);
		} catch (BluetoothStateException e) {
//			e.printStackTrace();
		}

		if (started) {
			try {
				synchronized (lock) {
					lock.wait();
				}
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}
		
		return devices;
	}

	@Override
	public String getServiceConnectionURL(Object serviceID, IDevice serviceHost) {
		UUID serviceUUID = (UUID) serviceID;
		LocalDevice lDev = null;
		try {
			lDev = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		final Object lock = new Object();
		final String[] address = new String[1];
		
		DiscoveryListener l = new DiscoveryListener() {

			@Override
			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				System.out.println("discovered device");
			}

			@Override
			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				System.out.println("discovered service");
				for (ServiceRecord sr : servRecord) {
					try {
						if (sr.getHostDevice().getFriendlyName(false).equals(serviceHost.getDeviceName())) {
							address[0] = sr.getConnectionURL(0, false);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void serviceSearchCompleted(int transID, int respCode) {
				synchronized (lock) {
					System.out.println("service search over");
					lock.notifyAll();
				}
			}

			@Override
			public void inquiryCompleted(int discType) {
				synchronized (lock) {
					System.out.println("inquiry over");
					lock.notifyAll();
				}
			}
			
		};
		boolean started = true;
		try {
			started = lDev.getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, l);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		System.out.println("searching for serviceID: " + serviceUUID);
		if (started) {
			try {
				synchronized (lock) {
					lock.wait();
					try {
						System.out.println("service discovery begin for: " + serviceUUID);
						lDev.getDiscoveryAgent().searchServices(null, new UUID[] {serviceUUID}, (RemoteDevice) serviceHost.getDeviceObject(), l);
					} catch (BluetoothStateException e) {
						e.printStackTrace();
					}
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return address[0];
	}

	@Override
	public String getConnectionTargetAddress(IConnectionObject connObject) {
		String result = null;
		try {
			result = RemoteDevice.getRemoteDevice((StreamConnection) connObject.getConnectionObject()).getBluetoothAddress();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void closeConnectionObject(IConnectionObject connObject) {
		try {
			((StreamConnection) connObject.getConnectionObject()).close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream openInputStream(IConnectionObject connObject) {
		InputStream is = null;
		try {
			is = ((StreamConnection) connObject.getConnectionObject()).openInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	@Override
	public OutputStream openOutputStream(IConnectionObject connObject) {
		OutputStream os = null;
		try {
			os = ((StreamConnection) connObject.getConnectionObject()).openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os;
	}

	@Override
	public String getServiceURL(IService service) {
//		System.out.println("without UUID cast: " + "btspp://localhost:" + this.serviceID + ";name=" + service.getName());
//		System.out.println("with UUID cast: btspp://localhost:" + ((UUID) service.getID()) + ";name=" + service.getName());
		return "btspp://localhost:" + ((UUID) service.getID()) + ";name=" + service.getName();
	}

	@Override
	public IServiceInfo getServiceInfo() {
		return new ServiceInfo(this.serviceID, this.serviceName);
	}
}
