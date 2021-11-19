package external.broadcaster;

import external.connection.ConnectionContainer;

public abstract class Broadcaster implements IBroadcaster {
	private ConnectionContainer cc;
	protected Broadcaster(ConnectionContainer cc) {
		this.cc = cc;
	}
	@Override
	public void broadcast() {
		this.cc.broadcastMessage(this.createMessage());
	}
}