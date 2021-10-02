package model.connectivity;

public class ClientData implements IClientData {
	
	private String clientName;
	private String clientAddress;
	private boolean isAllowedToConnect;
	private boolean isConnected;
	
	ClientData(String clientName, String clientAddress, boolean isAllowedToConnect, boolean isConnected) {
		this.clientName = clientName;
		this.clientAddress = clientAddress;
		this.isAllowedToConnect = isAllowedToConnect;
		this.isConnected = isConnected;
	}
	
	@Override
	public String getClientName() {
		return this.clientName;
	}

	@Override
	public String getClientAddress() {
		return this.clientAddress;
	}

	@Override
	public boolean getIsAllowedToConnect() {
		return this.isAllowedToConnect;
	}

	@Override
	public boolean getIsConnected() {
		return this.isConnected;
	}
	
}
