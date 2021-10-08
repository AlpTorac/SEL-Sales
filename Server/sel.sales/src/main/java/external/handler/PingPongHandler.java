package external.handler;

import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import external.message.IMessageParser;

public class PingPongHandler extends MessageHandler {

	private IPingPong pingPong;
	
	public PingPongHandler(IMessageParser parser, IPingPong pingPong) {
		super(parser);
		this.pingPong = pingPong;
	}

	@Override
	public boolean verify(IMessage message) {
		return message.isPingPongMessage();
	}

	@Override
	public boolean performNeededAction(IMessage message) {
//		System.out.println("Pingpong being sent to ping pong");
		this.pingPong.receiveResponse(message);
//		System.out.println("Pingpong response sent");
		return true;
	}

}
