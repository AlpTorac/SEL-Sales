package external.bluetooth;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class BluetoothClientManager extends ClientManager {

	public BluetoothClientManager(ExecutorService es, IController controller) {
		super(es, controller);
	}

	@Override
	public BluetoothClient getClient(String clientAddress) {
		return (BluetoothClient) super.getClient(clientAddress);
	}
	
	@Override
	public ClientDiscoveryStrategy initDiscoveryStrategy() {
		return new BluetoothClientDiscoveryStrategy();
	}
}
