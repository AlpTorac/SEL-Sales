package external.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ClientManager implements IClientManager {

	private static boolean INITIAL_CONNECTION_ALLOWANCE = true;
	private ClientDiscoveryStrategy cds;
	private Map<String, IClient> discoveredClients;
	private Map<String, ClientManagerEntry> knownClients;
	
	public ClientManager() {
		this.discoveredClients = new ConcurrentHashMap<String, IClient>();
		this.knownClients = new ConcurrentHashMap<String, ClientManagerEntry>();
		this.cds = this.initDiscoveryStrategy();
		this.discoverClients();
	}
	
	@Override
	public boolean isAllowedToConnect(String clientAddress) {
		ClientManagerEntry e = this.getClientEntry(clientAddress);
		if (e == null) {
			return false;
		}
		return e.isAllowedToConnect();
	}

	@Override
	public void addClient(String clientAddress) {
		IClient client = this.discoveredClients.get(clientAddress);
		if (client != null) {
			ClientManagerEntry e = new ClientManagerEntry(client, INITIAL_CONNECTION_ALLOWANCE);
			this.knownClients.put(clientAddress, e);
		}
	}

	private ClientManagerEntry getClientEntry(String clientAddress) {
		return this.knownClients.get(clientAddress);
	}
	
	@Override
	public IClient getClient(String clientAddress) {
		ClientManagerEntry wantedClientEntry = this.getClientEntry(clientAddress);
		if (wantedClientEntry == null) {
			return null;
		}
		return wantedClientEntry.getClient();
	}
	
	@Override
	public void removeClient(String clientAddress) {
		this.knownClients.remove(clientAddress);
	}

	@Override
	public void allowClient(String clientAddress) {
		ClientManagerEntry e = this.getClientEntry(clientAddress);
		if (e != null) {
			e.setConnectionAllowance(true);
		}
	}

	@Override
	public void blockClient(String clientAddress) {
		ClientManagerEntry e = this.getClientEntry(clientAddress);
		if (e != null) {
			e.setConnectionAllowance(false);
		}
	}

	public void discoverClients() {
		Collection<IClient> discoveredClients = this.cds.discoverClients();
		
		//Notify external
		
		if (discoveredClients != null) {
			for (IClient c : discoveredClients) {
				this.discoveredClients.put(c.getClientAddress(), c);
			}
		}
	}
	
	@Override
	public void setDiscoveryStrategy(ClientDiscoveryStrategy cds) {
		this.cds = cds;
	}
	
	@Override
	public int getClientCount() {
		return this.knownClients.size();
	}
	
	@Override
	public Collection<IClient> getDiscoveredClients() {
		return this.discoveredClients.values();
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
