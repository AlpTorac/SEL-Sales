package external.bluetooth;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

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

import controller.IController;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.connection.StandardConnectionManager;
import external.connection.outgoing.ExternalConnector;
import external.device.IDevice;

public class BluetoothExternalConnector extends ExternalConnector {

	public BluetoothExternalConnector(IService service, IController controller, ExecutorService es, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(service, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}

	@Override
	protected IConnection initConnection(Object connectionObject) {
		return new BluetoothConnection((StreamConnection) connectionObject);
	}

	@Override
	protected Object getConnectionObject(Object serviceID, IDevice serviceHost) {
		StreamConnection conn = null;
		try {
			String address = this.getConnectionAddress(serviceID, serviceHost);
			System.out.println("Connecting to service: " + address);
			conn = (StreamConnection) Connector.open(address);
			System.out.println("Connected to service");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return conn;
	}

	@Override
	protected IConnectionManager createConnectionManager(IConnection conn, long pingPongTimeout, long sendTimeout,
			int resendLimit, long minimalPingPongDelay) {
		return new StandardConnectionManager(controller, conn, es, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay);
	}

	@Override
	protected String getConnectionAddress(Object serviceID, IDevice serviceHost) {
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
}
