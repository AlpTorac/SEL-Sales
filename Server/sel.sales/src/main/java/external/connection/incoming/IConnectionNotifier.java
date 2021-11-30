package external.connection.incoming;

import external.connection.IConnectionObject;
import external.connection.IService;

public interface IConnectionNotifier {
	Object getConnectionNotifierObject();
	IConnectionObject acceptAndOpen();
	IService getService();
}
