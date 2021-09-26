package test.external.dummy;

import external.client.IClient;

public class DummyClient implements IClient {
	private String clientName;
	private String clientAddress;
	
	public DummyClient(String clientName, String clientAddress) {
		this.clientName = clientName;
		this.clientAddress = clientAddress;
	}
	
	@Override
	public String getClientName() {
		return this.clientName;
	}

	@Override
	public String getClientAddress() {
		return this.clientAddress;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof IClient)) {
			return false;
		}
		return this.getClientAddress().equals(((IClient) o).getClientAddress());
	}
}
