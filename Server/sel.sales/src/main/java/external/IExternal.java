package external;

import external.connection.IHasConnectionSettings;
import model.ExternalUpdatable;
import model.KnownDeviceUpdatable;
import model.SettingsUpdatable;

public interface IExternal extends ExternalUpdatable, KnownDeviceUpdatable, SettingsUpdatable, IHasConnectionSettings {
	void close();
}
