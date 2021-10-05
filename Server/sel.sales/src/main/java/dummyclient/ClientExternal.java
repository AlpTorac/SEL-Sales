package dummyclient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
import external.bluetooth.BluetoothClientManager;
import external.bluetooth.BluetoothConnection;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.IService;
import external.connection.StandardConnectionManager;
import external.message.IMessage;
import external.message.Message;
import model.IModel;

public class ClientExternal extends External {

	private RemoteDevice server;
	private ServiceRecord serviceRecord;
	private IConnectionManager connManager;
	
	protected ClientExternal(IController controller, IModel model) {
		super(controller, model);
		// TODO Auto-generated constructor stub
	}
	
	public IConnectionManager getConnManager() {
		return this.connManager;
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
							serviceRecord = sr;
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
		
		return serviceRecord.getConnectionURL(0, false);
	}

	public void connectToService() {
		StreamConnection conn = null;
		try {
			System.out.println("Connecting to service");
			conn = (StreamConnection) Connector.open(this.getConnectionURL());
			System.out.println("Connected to service");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		IConnection connwrapper = new BluetoothConnection((StreamConnection) conn);
		
		IConnectionManager connManager = new StandardConnectionManager(this.getController(), connwrapper, es);
		this.connManager = connManager;
	}

	protected BluetoothServiceConnectionManager getServiceConnectionManager() {
		return (BluetoothServiceConnectionManager) super.getService().getServiceConnectionManager();
	}
	
	protected BluetoothClientManager getClientManager() {
		return (BluetoothClientManager) super.getService().getClientManager();
	}
	
	@Override
	protected BluetoothService getService() {
		return (BluetoothService) super.getService();
	}
	
	protected BluetoothClientManager initClientManager() {
		return new BluetoothClientManager(es, this.getController());
	}
	
	@Override
	protected BluetoothService initService() {
		return this.initBluetoothService(new UUID(0x1111), "SEL_Service");
	}
	
	private BluetoothService initBluetoothService(UUID id, String name) {
		return new BluetoothService(id, name, this.initClientManager(), this.getController(), es);
	}
	@Override
	public void rediscoverClients() {
//		this.service.getClientManager().discoverClients();
	}
	@Override
	public void refreshKnownClients() {
//		this.service.getClientManager().receiveKnownClientData(this.model.getAllKnownClientData());
	}
	
	public void close(IMessage ackMessage) {
		System.out.println("Closing");
		this.connManager.getSendBuffer().receiveAcknowledgement(ackMessage);
		this.connManager.close();
		System.out.println("ConnManager closing");
		try {
			this.es.awaitTermination(3000, TimeUnit.MILLISECONDS);
			System.out.println("Pool shutdown");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
