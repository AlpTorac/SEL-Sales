package external.bluetooth;

import controller.IController;
import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class BluetoothClientManager extends ClientManager {

	public BluetoothClientManager(IController controller) {
		super(controller);
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
