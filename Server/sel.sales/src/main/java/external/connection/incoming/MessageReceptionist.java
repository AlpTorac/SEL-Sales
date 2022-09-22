package external.connection.incoming;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.acknowledgement.StandardAcknowledger;
import external.connection.IConnection;
import external.connection.outgoing.BasicMessageSender;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.handler.AcknowledgementHandler;
import external.handler.IMessageHandler;
import external.handler.OrderHandler;
import external.handler.PingPongHandler;
import external.message.IMessageParser;
import external.message.StandardMessageParser;

public class MessageReceptionist implements IMessageReceptionist {
	protected ExecutorService es;
	private Collection<IMessageHandler> messageHandlers;
	private IMessageReadingStrategy mrs;
	private IMessageParser messageParser;
	private IConnection conn;
	private IController controller;
	private ISendBuffer sendBuffer;
	private IPingPong pingPong;
	
	public MessageReceptionist(IConnection conn, IController controller, ISendBuffer sendBuffer, IPingPong pingPong, ExecutorService es) {
		this.es = es;
		this.conn = conn;
		this.controller = controller;
		this.sendBuffer = sendBuffer;
		this.pingPong = pingPong;
		this.messageParser = this.initMessageParser();
		this.messageHandlers = this.initMessageHandlers();
		this.mrs = this.initMessageReadingStrategy();
	}
	
	protected boolean handleMessage(String message) {
		return this.messageHandlers.stream().anyMatch(h -> h.handleMessage(message));
	}

	protected Collection<IMessageHandler> initMessageHandlers() {
		Collection<IMessageHandler> col = new CopyOnWriteArrayList<IMessageHandler>();
		col.add(new OrderHandler(this.messageParser, new StandardAcknowledger(this.conn), this.controller));
		col.add(new AcknowledgementHandler(this.messageParser, this.sendBuffer));
		col.add(new PingPongHandler(this.messageParser, this.pingPong, this.conn, new BasicMessageSender()));
		return col;
	}

	protected IMessageReadingStrategy initMessageReadingStrategy() {
		return new StandardReader();
	}

	protected IMessageParser initMessageParser() {
		return new StandardMessageParser();
	}

	@Override
	public void close() {
		this.messageHandlers.clear();
	}

	@Override
	public boolean checkForMessages() {
		String[] serialisedMessages;
		boolean allHandled = true;
		if ((serialisedMessages = this.mrs.readMessages(this.conn.getInputStream())) != null) {
			for (String m : serialisedMessages) {
				System.out.println("Message received: " + m);
				allHandled = allHandled & this.handleMessage(m);
			}
			this.conn.refreshInputStream();
		}
		return allHandled;
	}
}
