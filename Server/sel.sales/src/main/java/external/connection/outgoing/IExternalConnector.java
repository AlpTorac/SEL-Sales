package external.connection.outgoing;

import external.connection.ConnectionContainer;
import external.connection.IHasConnectionSettings;

public interface IExternalConnector extends ConnectionContainer, IHasConnectionSettings {
	void connectToService(String serviceID, String deviceAddress);
}
