package external.broadcaster;

import external.connection.IServiceConnectionManager;

public abstract class Broadcaster implements IBroadcaster {
	private IServiceConnectionManager scm;
	protected Broadcaster(IServiceConnectionManager scm) {
		this.scm = scm;
	}
	@Override
	public void broadcast() {
		this.scm.broadcastMessage(this.createMessage());
	}
}