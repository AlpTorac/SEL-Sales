package dummyclient;

import java.io.IOException;

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

import controller.IController;
import external.External;
import external.bluetooth.BluetoothConnection;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.connection.StandardConnectionManager;
import external.message.IMessage;
import model.IModel;

public class ClientExternal extends External {

	private RemoteDevice server;
	private ServiceRecord service;
	private IConnectionManager connManager;
	
	protected ClientExternal(IController controller, IModel model) {
		super(controller, model);
		// TODO Auto-generated constructor stub
	}
	
	public void sendMessage(IMessage message) {
		connManager.sendMessage(message);
	}
	
	public boolean isConnected() {
		return connManager != null && !connManager.getConnection().isClosed();
	}
	
	private String getConnectionURL() {
		LocalDevice lDev = null;
		try {
			lDev = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		final Object lock = new Object();
		
		DiscoveryListener l = new DiscoveryListener() {

			@Override
			public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
				try {
					if (btDevice.getFriendlyName(false).equals("DESKTOP-DIRGCI8")) {
						server = btDevice;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
				for (ServiceRecord sr : servRecord) {
					try {
						if (sr.getHostDevice().getFriendlyName(false).equals(server.getFriendlyName(false))) {
							service = sr;
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
					lock.notifyAll();
				}
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
			e.printStackTrace();
		}

		if (started) {
			try {
				synchronized (lock) {
					lock.wait();
					try {
						lDev.getDiscoveryAgent().searchServices(null, new UUID[] {new UUID(0x1111)}, server, l);
					} catch (BluetoothStateException e) {
						e.printStackTrace();
					}
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return service.getConnectionURL(0, false);
	}

	public void connectToService() {
		es.submit(() -> {
			StreamConnectionNotifier connNotifier = null;
			try {
				connNotifier = (StreamConnectionNotifier) Connector.open(this.getConnectionURL());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			StreamConnection connectionObject = null;
			
			try {
				connectionObject = connNotifier.acceptAndOpen();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			IConnection conn = new BluetoothConnection((StreamConnection) connectionObject);
			
			IConnectionManager connManager = new StandardConnectionManager(this.getController(), conn, es);
			this.connManager = connManager;
		});
	}

	@Override
	protected IService initService() {
		return null;
	}
	protected void setService(IService service) {
		
	}
	@Override
	public void rediscoverClients() {
		System.out.println("External rediscovering clients");
	}
	@Override
	public void refreshKnownClients() {
		System.out.println("External refreshing known clients");
	}
}
