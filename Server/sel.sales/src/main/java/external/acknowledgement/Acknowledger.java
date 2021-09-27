package external.acknowledgement;

import java.io.OutputStream;

import external.connection.IMessageSendingStrategy;
import external.message.IMessage;

public abstract class Acknowledger implements IAcknowledger {
	
	private IAcknowledgementStrategy ackStrategy;
	private IMessageSendingStrategy mss;
	private OutputStream os;
	
	Acknowledger(OutputStream os, IAcknowledgementStrategy ackStrategy, IMessageSendingStrategy mss) {
		this.os = os;
		this.ackStrategy = ackStrategy;
		this.mss = mss;
	}
	
	@Override
	public boolean acknowledge(IMessage message) {
		return !message.isAcknowledgementMessage() &&
				this.mss.sendMessage(this.os, this.ackStrategy.generateAcknowledgementMessage(message));
	}

}
