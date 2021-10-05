package model.connectivity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectivityManager implements IConnectivityManager {

	private Map<String, ConnectivityManagerEntry> discoveredClients;
	private Map<String, ConnectivityManagerEntry> knownClients;
	
	private boolean rediscover;
	
	public ConnectivityManager() {
		rediscover = true;
		this.discoveredClients = new ConcurrentHashMap<String, ConnectivityManagerEntry>();
		this.knownClients = new ConcurrentHashMap<String, ConnectivityManagerEntry>();
	}
	
	@Override
	public void addDiscoveredClient(String clientName, String clientAddress) {
		this.rediscover = false;
		this.discoveredClients.put(clientAddress, new ConnectivityManagerEntry(clientName,clientAddress,false));
	}

	@Override
	public void addKnownClient(String clientAddress) {
		this.knownClients.put(clientAddress, new ConnectivityManagerEntry(this.discoveredClients.get(clientAddress).getClientName(),clientAddress,true));
	}

	@Override
	public void removeKnownClient(String clientAddress) {
		this.knownClients.remove(clientAddress);
	}

	@Override
	public void allowKnownClient(String clientAddress) {
		this.knownClients.get(clientAddress).setConnectionAllowance(true);
	}

	@Override
	public void blockKnownClient(String clientAddress) {
		this.knownClients.get(clientAddress).setConnectionAllowance(false);
	}

	@Override
	public boolean isAllowedToConnect(String clientAddress) {
		return this.knownClients.get(clientAddress).isAllowedToConnect();
	}

	@Override
	public boolean isConnected(String clientAddress) {
		return this.knownClients.get(clientAddress).isConnected();
	}

	@Override
	public int getKnownClientCount() {
		return this.knownClients.size();
	}
	
	@Override
	public IClientData[] getAllKnownClientData() {
		return this.knownClients.values().stream()
		.map(e -> new ClientData(e.getClientName(), e.getClientAddress(), e.isAllowedToConnect(), e.isConnected()))
		.toArray(IClientData[]::new);
	}
	
	@Override
	public IClientData[] getAllDiscoveredClientData() {
		return this.discoveredClients.values().stream()
				.map(e -> new ClientData(e.getClientName(), e.getClientAddress(), e.isAllowedToConnect(), e.isConnected()))
				.toArray(IClientData[]::new);
	}
	
	@Override
	public void clientConnected(String clientAddress) {
//		this.knownClients.get(clientAddress).setConnectionStatus(true);
	}

	@Override
	public void clientDisconnected(String clientAddress) {
//		this.knownClients.get(clientAddress).setConnectionStatus(false);
	}
	
	@Override
	public boolean isClientRediscoveryRequested() {
		return this.rediscover;
	}
	
	@Override
	public void requestClientRediscovery() {
		this.rediscover = true;
	}
	
	private class ConnectivityManagerEntry {
		private String clientName;
		private String clientAddress;
		private boolean isAllowedToConnect;
		private boolean isConnected;
		
		private ConnectivityManagerEntry(String clientName, String clientAddress, boolean isAllowedToConnect) {
			this.clientName = clientName;
			this.clientAddress = clientAddress;
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		public String getClientName() {
			return this.clientName;
		}
		
		public String getClientAddress() {
			return this.clientAddress;
		}
		
		public boolean isAllowedToConnect() {
			return this.isAllowedToConnect;
		}
		
		public boolean isConnected() {
			return this.isConnected;
		}
		
		public void setConnectionAllowance(boolean isAllowedToConnect) {
			this.isAllowedToConnect = isAllowedToConnect;
		}
		
		public void setConnectionStatus(boolean isConnected) {
			this.isConnected = isConnected;
		}
	}
}
