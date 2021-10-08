package external.handler;

import external.connection.outgoing.ISendBuffer;
import external.message.IMessage;
import external.message.IMessageParser;

public class AcknowledgementHandler extends MessageHandler {

	private ISendBuffer sendBuffer;
	
	public AcknowledgementHandler(IMessageParser parser, ISendBuffer sendBuffer) {
		super(parser);
		this.sendBuffer = sendBuffer;
	}
	
	@Override
	public boolean verify(IMessage message) {
		return message.isAcknowledgementMessage();
	}

	@Override
	public boolean performNeededAction(IMessage message) {
//		System.out.println("Acknowledgement being sent to send buffer");
		this.sendBuffer.receiveAcknowledgement(message);
//		System.out.println("Acknowledgement sent to send buffer");
		return !this.sendBuffer.isBlocked();
	}
}
