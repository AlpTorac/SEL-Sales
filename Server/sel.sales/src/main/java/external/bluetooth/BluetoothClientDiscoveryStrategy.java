package external.bluetooth;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import external.client.ClientDiscoveryStrategy;
import external.client.IClient;

public class BluetoothClientDiscoveryStrategy extends ClientDiscoveryStrategy {
	@Override
	public Collection<IClient> discoverClients() {
		Collection<IClient> clients = new CopyOnWriteArrayList<IClient>();
		LocalDevice lDev = null;
		try {
			lDev = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		RemoteDevice[] devices = lDev.getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);
		for (RemoteDevice rd : devices) {
			clients.add(new BluetoothClient(rd));
		}
		return clients;
	}
}
