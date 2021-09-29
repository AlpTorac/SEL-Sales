package model.connectivity;

public interface IClientDataFactory {
	IClientData constructClientData(String clientName, String clientAddress, boolean isAllowedToConnect);
}
