package external;

public interface IClient {
	String getClientName();
	String getClientAddress();
	String getClientID();
	boolean areIDsEqual(String id);
	boolean equals(Object o);
}
