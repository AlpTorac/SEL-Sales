package external.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ClientManager implements IClientManager {

	private static boolean INITIAL_CONNECTION_ALLOWANCE = true;
	private ClientDiscoveryStrategy cds;
	private Collection<ClientManagerEntry> clients;
	
	public ClientManager() {
		this.clients = new CopyOnWriteArrayList<ClientManagerEntry>();
		this.cds = this.initDiscoveryStrategy();
	}
	
	@Override
	public boolean isAllowedToConnect(String clientAddress) {
		return this.getClientEntry(clientAddress).isAllowedToConnect();
	}

	@Override
	public void addClient(IClient client) {
		ClientManagerEntry e = new ClientManagerEntry(client, INITIAL_CONNECTION_ALLOWANCE);
		this.clients.add(e);
	}

	private ClientManagerEntry getClientEntry(String clientAddress) {
		return this.clients.stream().filter(e -> e.getClient().getClientAddress().equals(clientAddress)).findFirst().get();
	}
	
	@Override
	public IClient getClient(String clientAddress) {
		return this.getClientEntry(clientAddress).getClient();
	}
	
	@Override
	public void removeClient(String clientAddress) {
		this.clients.removeIf(e -> e.getClient().getClientAddress().equals(clientAddress));
	}

	@Override
	public void allowClient(String clientAddress) {
		Iterator<ClientManagerEntry> it = this.clients.iterator();
		while (it.hasNext()) {
			ClientManagerEntry current = it.next();
			if (current.getClient().getClientAddress().equals(clientAddress)) {
				current.setConnectionAllowance(true);
				return;
			}
		}
	}

	@Override
	public void blockClient(String clientAddress) {
		Iterator<ClientManagerEntry> it = this.clients.iterator();
		while (it.hasNext()) {
			ClientManagerEntry current = it.next();
			if (current.getClient().getClientAddress().equals(clientAddress)) {
				current.setConnectionAllowance(false);
				return;
			}
		}
	}

	public Collection<IClient> discoverClients() {
		return this.cds.discoverClients();
	}
	
	@Override
	public void setDiscoveryStrategy(ClientDiscoveryStrategy cds) {
		this.cds = cds;
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