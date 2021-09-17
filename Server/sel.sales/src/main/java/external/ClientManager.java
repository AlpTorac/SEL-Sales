package external;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ClientManager implements IClientManager {

	private ClientDiscoveryStrategy cds;
	private Collection<ClientManagerEntry> clients;
	
	public ClientManager() {
		this.clients = new CopyOnWriteArrayList<ClientManagerEntry>();
		this.cds = this.initDiscoveryStrategy();
	}
	
	@Override
	public boolean isAllowedToConnect(String deviceID) {
		return this.clients.stream().filter(e -> e.getClient().areIDsEqual(deviceID)).findFirst().get().isAllowedToConnect();
	}

	@Override
	public void addClient(IClient client) {
		ClientManagerEntry e = new ClientManagerEntry(client, false);
		this.clients.add(e);
	}

	@Override
	public void removeClient(String deviceID) {
		this.clients.removeIf(e -> e.getClient().areIDsEqual(deviceID));
	}

	@Override
	public void allowClient(String deviceID) {
		Iterator<ClientManagerEntry> it = this.clients.iterator();
		while (it.hasNext()) {
			ClientManagerEntry current = it.next();
			if (current.getClient().areIDsEqual(deviceID)) {
				current.setConnectionAllowance(true);
				return;
			}
		}
	}

	@Override
	public void blockClient(String deviceID) {
		Iterator<ClientManagerEntry> it = this.clients.iterator();
		while (it.hasNext()) {
			ClientManagerEntry current = it.next();
			if (current.getClient().areIDsEqual(deviceID)) {
				current.setConnectionAllowance(false);
				return;
			}
		}
	}

	public Collection<IClient> discoverClients() {
		return this.cds.discoverClients();
	}
	
	private class ClientManagerEntry {
		private IClient client;
		private boolean isAllowedToConnect;
		
		private ClientManagerEntry(IClient client, boolean isAllowedToConnect) {
			this.client = client;
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		private IClient getClient() {
			return this.client;
		}
		
		private boolean isAllowedToConnect() {
			return this.isAllowedToConnect;
		}
		
		private void setConnectionAllowance(boolean isAllowedToConnect) {
			this.isAllowedToConnect = isAllowedToConnect;
		}
	}
}
