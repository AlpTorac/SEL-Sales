package controller;

public enum StatusEvent implements IApplicationEvent {
	KNOWN_CLIENT_ADDED,
	CLIENT_DISCOVERED,
	CLIENT_CONNECTED,
	CLIENT_DISCONNECTED;
}
