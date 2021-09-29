package model.connectivity;

public class ClientData implements IClientData {
	
	private String clientName;
	private String clientAddress;
	private boolean isAllowedToConnect;
	
	ClientData(String clientName, String clientAddress, boolean isAllowedToConnect) {
		this.clientName = clientName;
		this.clientAddress = clientAddress;
		this.isAllowedToConnect = isAllowedToConnect;
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

}
