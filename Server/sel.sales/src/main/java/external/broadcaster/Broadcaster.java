package external.broadcaster;

import external.connection.IServiceConnectionManager;
import external.message.IMessage;

public abstract class Broadcaster implements IBroadcaster {
	private IServiceConnectionManager scm;
	Broadcaster(IServiceConnectionManager scm) {
		this.scm = scm;
	}
	@Override
	public void broadcast() {
		this.scm.broadcastMessage(this.createMessage());
	}
	protected abstract IMessage createMessage();
}