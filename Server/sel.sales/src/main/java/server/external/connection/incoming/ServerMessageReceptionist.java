package server.external.connection.incoming;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.IConnection;
import external.connection.incoming.MessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.handler.IMessageHandler;

public class ServerMessageReceptionist extends MessageReceptionist {

	public ServerMessageReceptionist(IConnection conn, IController controller, ISendBuffer sendBuffer,
			IPingPong pingPong, ExecutorService es) {
		super(conn, controller, sendBuffer, pingPong, es);
	}

	@Override
	protected Collection<IMessageHandler> initMessageHandlers() {
		Collection<IMessageHandler> col = super.initMessageHandlers();
		
		return col;
	}
}
