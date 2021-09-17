package external;

import java.util.Collection;

public abstract class IncomingMessageReader implements IIncomingMessageReader {
	private IConnection conn;
	private Collection<IMessageHandler> messageHandlers;
	private IMessageReadingStrategy mrs;
	
	IncomingMessageReader() {
		this.messageHandlers = this.initMessageHandlers();
	}
	
	@Override
	public boolean handleMessage(String message) {
		return this.messageHandlers.stream().anyMatch(h -> h.handleMessage(message));
	}

	@Override
	public IConnection getConnection() {
		return this.conn;
	}

	@Override
	public void setMessageReadingStrategy(IMessageReadingStrategy mrs) {
		this.mrs = mrs;
	}

	@Override
	public IMessageReadingStrategy getMessageReadingStrategy() {
		return this.mrs;
	}

}
