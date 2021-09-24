package external.bluetooth;

import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;

public class BluetoothClientManager extends ClientManager {

	@Override
	public BluetoothClient getClient(String clientAddress) {
		return (BluetoothClient) super.getClient(clientAddress);
	}
	
	@Override
	public ClientDiscoveryStrategy initDiscoveryStrategy() {
		return new BluetoothClientDiscoveryStrategy();
	}
}
