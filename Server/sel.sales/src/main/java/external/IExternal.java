package external;

import model.ExternalUpdatable;
import model.KnownClientUpdatable;
import model.SettingsUpdatable;

public interface IExternal extends ExternalUpdatable, KnownClientUpdatable, SettingsUpdatable {
	void close();
	long getMinimalPingPongDelay();
	int getResendLimit();
	long getPingPongTimeout();
	long getSendTimeout();
}
