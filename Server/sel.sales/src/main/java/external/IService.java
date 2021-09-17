package external;

public interface IService {
	String getID();
	String getName();
	String getURL();
	IServiceConnectionManager publish();
	IServiceConnectionManager getServiceConnectionManager();
	void generateAndSetURL();
}
