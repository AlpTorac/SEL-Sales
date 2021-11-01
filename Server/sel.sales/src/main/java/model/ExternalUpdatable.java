package model;

public interface ExternalUpdatable extends Updatable {
	void rediscoverClients(Runnable afterDiscoveryAction);
}
