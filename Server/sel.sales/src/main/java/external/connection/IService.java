package external.connection;

import external.device.IDeviceManager;
import model.connectivity.IDeviceData;
import model.settings.ISettings;

public interface IService {
	String getID();
	String getName();
	String getURL();
	void publish();
	IServiceConnectionManager getServiceConnectionManager();
	IDeviceManager getDeviceManager();
	String generateURL();
	void receiveKnownDeviceData(IDeviceData[] deviceData);
	long getMinimalPingPongDelay();
	int getResendLimit();
	long getPingPongTimeout();
	long getSendTimeout();
	void receiveSettings(ISettings settings);
}
