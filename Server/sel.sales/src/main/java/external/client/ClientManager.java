package external.client;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import controller.IController;
import model.connectivity.IClientData;

public abstract class ClientManager implements IClientManager {

	protected ExecutorService es;
	private static boolean INITIAL_CONNECTION_ALLOWANCE = true;
	private ClientDiscoveryStrategy cds;
	private Map<String, IClient> discoveredClients;
	private Map<String, ClientManagerEntry> knownClients;
	
	private ClientDiscoveryListener cdl;
	
	public ClientManager(ExecutorService es) {
		this.discoveredClients = new ConcurrentHashMap<String, IClient>();
		this.knownClients = new ConcurrentHashMap<String, ClientManagerEntry>();
		this.es = es;
		this.cds = this.initDiscoveryStrategy();
	}
	
	public ClientManager(ExecutorService es, IController controller) {
		this.discoveredClients = new ConcurrentHashMap<String, IClient>();
		this.knownClients = new ConcurrentHashMap<String, ClientManagerEntry>();
		this.es = es;
		this.cds = this.initDiscoveryStrategy();
		this.cdl = new ClientDiscoveryListener(controller);
	}
	
	@Override
	public void setClientDiscoveryListener(ClientDiscoveryListener cdl) {
		this.cdl = cdl;
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
		es.submit(new Runnable() {
			@Override
			public void run() {
				Collection<IClient> dcs = cds.discoverClients();
				
				if (discoveredClients != null) {
					for (IClient c : dcs) {
						discoveredClients.put(c.getClientAddress(), c);
						if (cdl != null) {
							cdl.clientDiscovered(c.getClientName(), c.getClientAddress());
						}
					}
				}
			}
		});
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
	
	@Override
	public void receiveKnownClientData(IClientData[] clientData) {
		for (IClientData d : clientData) {
			if (!this.knownClients.containsKey(d.getClientAddress())) {
				this.addClient(d.getClientAddress());
			}
			if (d.getIsAllowedToConnect()) {
				this.allowClient(d.getClientAddress());
			} else {
				this.blockClient(d.getClientAddress());
			}
		}
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
