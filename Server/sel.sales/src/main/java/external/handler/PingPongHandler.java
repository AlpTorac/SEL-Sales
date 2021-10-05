package external.handler;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import external.message.IMessageParser;

public class PingPongHandler extends MessageHandler {

	private IMessageSendingStrategy mss;
	private IConnection conn;
	private IPingPong pingPong;
	
	public PingPongHandler(IMessageParser parser, IPingPong pingPong, IConnection conn, IMessageSendingStrategy mss) {
		super(parser);
		this.pingPong = pingPong;
		this.conn = conn;
		this.mss = mss;
	}

	@Override
	public boolean verify(IMessage message) {
		return message.isPingPongMessage();
	}

	@Override
	public boolean acknowledge(IMessage message) {
		System.out.println("Pingpong message verified");
		return true;
	}

	@Override
	public boolean performNeededAction(IMessage message) {
		System.out.println("Pingpong being sent to ping pong");
		pingPong.receiveResponse(message);
		System.out.println("Pingpong response sent");
		return this.mss.sendMessage(this.conn, message);
	}

}
