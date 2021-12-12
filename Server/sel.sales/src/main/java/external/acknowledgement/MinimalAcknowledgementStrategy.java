package external.acknowledgement;

import external.message.IMessage;

public class MinimalAcknowledgementStrategy implements IAcknowledgementStrategy {
	
	public MinimalAcknowledgementStrategy() {
		
	}
	
	@Override
	public IMessage generateAcknowledgementMessage(IMessage message) {
//		System.out.println("Sending ack with seqNumber: " + message.getSequenceNumber());
		return message.getMinimalAcknowledgementMessage();
	}
}
