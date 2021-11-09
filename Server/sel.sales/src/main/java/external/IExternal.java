package external;

import model.ExternalUpdatable;
import model.KnownDeviceUpdatable;
import model.SettingsUpdatable;

public interface IExternal extends ExternalUpdatable, KnownDeviceUpdatable, SettingsUpdatable {
	void close();
	long getMinimalPingPongDelay();
	int getResendLimit();
	long getPingPongTimeout();
	long getSendTimeout();
}
