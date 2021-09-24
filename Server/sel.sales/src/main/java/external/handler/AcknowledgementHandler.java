package external.handler;

import external.buffer.ISendBuffer;
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
	public boolean acknowledge(IMessage message) {
		return true;
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		this.sendBuffer.receiveAcknowledgement(message);
		return true;
	}
}
