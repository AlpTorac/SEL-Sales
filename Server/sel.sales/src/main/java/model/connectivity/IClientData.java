package model.connectivity;

public interface IClientData {
	String getClientName();
	String getClientAddress();
	boolean getIsAllowedToConnect();
	boolean getIsConnected();
	boolean equals(Object o);
}
