package external;

import model.ExternalUpdatable;
import model.KnownClientUpdatable;

public interface IExternal extends ExternalUpdatable, KnownClientUpdatable {
	void close();
	long getMinimalPingPongDelay();
	int getResendLimit();
	long getPingPongTimeout();
	long getSendTimeout();
}
