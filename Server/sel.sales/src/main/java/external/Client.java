package external;

public abstract class Client implements IClient {
	private String name;
	private String address;
	
	public Client(String name, String address) {
		this.name = name;
		this.address = address;
	}
	
	@Override
	public String getClientName() {
		return this.name;
	}

	@Override
	public String getClientAddress() {
		return this.address;
	}
}
