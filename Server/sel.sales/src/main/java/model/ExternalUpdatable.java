package model;

public interface ExternalUpdatable extends Updatable {
	void rediscoverDevices(Runnable afterDiscoveryAction);
}
