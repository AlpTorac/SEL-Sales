package external.acknowledgement;

import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.message.IMessage;

public abstract class Acknowledger implements IAcknowledger {
	
	private IAcknowledgementStrategy ackStrategy;
	private IMessageSendingStrategy mss;
	private IConnection conn;
	
	Acknowledger(IConnection conn, IAcknowledgementStrategy ackStrategy, IMessageSendingStrategy mss) {
		this.conn = conn;
		this.ackStrategy = ackStrategy;
		this.mss = mss;
	}
	
	@Override
	public boolean acknowledge(IMessage message) {
		return !message.isAcknowledgementMessage() &&
				this.mss.sendMessage(this.conn, this.ackStrategy.generateAcknowledgementMessage(message));
	}

}
