package model.connectivity;

public class ClientDataFactory implements IClientDataFactory {
	
	public ClientDataFactory() {}
	
	@Override
	public ClientData constructClientData(String clientName, String clientAddress, boolean isAllowedToConnect) {
		return new ClientData(clientName, clientAddress, isAllowedToConnect);
	}
}
