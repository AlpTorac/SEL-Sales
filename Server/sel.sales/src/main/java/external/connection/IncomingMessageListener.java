package external.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import external.acknowledgement.StandardAcknowledger;
import external.buffer.ISendBuffer;
import external.handler.AcknowledgementHandler;
import external.handler.IMessageHandler;
import external.handler.OrderHandler;
import external.message.IMessageParser;
import external.message.StandardMessageParser;

public class IncomingMessageListener implements IIncomingMessageListener {
	private Collection<IMessageHandler> messageHandlers;
	private IMessageReadingStrategy mrs;
	private IMessageParser messageParser;
	private InputStream is;
	private OutputStream os;
	private IController controller;
	private ISendBuffer sendBuffer;
	
	public IncomingMessageListener(InputStream is, OutputStream os, IController controller, ISendBuffer sendBuffer) {
		this.is = is;
		this.os = os;
		this.controller = controller;
		this.sendBuffer = sendBuffer;
		this.messageParser = this.initMessageParser();
		this.messageHandlers = this.initMessageHandlers();
		this.mrs = this.initMessageReadingStrategy();
	}
	
	@Override
	public boolean handleMessage(String message) {
		return this.messageHandlers.stream().anyMatch(h -> h.handleMessage(message));
	}

	@Override
	public Collection<IMessageHandler> initMessageHandlers() {
		Collection<IMessageHandler> col = new CopyOnWriteArrayList<IMessageHandler>();
		col.add(new OrderHandler(this.messageParser, new StandardAcknowledger(this.os), this.controller));
		col.add(new AcknowledgementHandler(this.messageParser, this.sendBuffer));
		return col;
	}

	@Override
	public IMessageReadingStrategy initMessageReadingStrategy() {
		return new LineReader(is);
	}

	@Override
	public IMessageParser initMessageParser() {
		return new StandardMessageParser();
	}

	@Override
	public boolean handleCurrentMessage() {
		String message = this.mrs.readMessage();
		if (message != null && !message.equals("")) {
			return this.handleMessage(message);
		}
		return false;
	}

}
