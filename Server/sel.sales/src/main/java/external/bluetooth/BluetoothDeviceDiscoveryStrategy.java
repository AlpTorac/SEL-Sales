package external.bluetooth;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import external.device.DeviceDiscoveryStrategy;
import external.device.IDevice;

public class BluetoothDeviceDiscoveryStrategy extends DeviceDiscoveryStrategy {
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
				devices.add(new BluetoothDevice(btDevice));
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
}
