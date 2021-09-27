package external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.buffer.ISendBuffer;
import external.message.IMessage;

public abstract class ConnectionManager implements IConnectionManager {
	private ExecutorService es;
	private IConnection conn;
	private IIncomingMessageListener iml;
	private ISendBuffer sb;
	protected IController controller;
	
	protected ConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		this.controller = controller;
		this.conn = conn;
		this.es = es;
	}
	
	@Override
	public IConnection getConnection() {
		return this.conn;
	}
	
	protected ExecutorService getExecutorService() {
		return this.es;
	}
	
	protected abstract ISendBuffer initSendBuffer();
	
	protected abstract IIncomingMessageListener initIncomingMessageListener();
	
	private void setIncomingMessageListener(IIncomingMessageListener iml) {
		this.iml = iml;
	}
	
	private void setSendBuffer(ISendBuffer sb) {
		this.sb = sb;
	}
	
	@Override
	public ISendBuffer getSendBuffer() {
		return this.sb;
	}
	@Override
	public IIncomingMessageListener getIncomingMessageListener() {
		return this.iml;
	}
	
	protected abstract Runnable[] initConnectionRunnables();
	
	protected Runnable initMessageReadingRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				IIncomingMessageListener incomingMessageListener = initIncomingMessageListener();
				setIncomingMessageListener(incomingMessageListener);
				while (!getConnection().isClosed()) {
					getIncomingMessageListener().handleCurrentMessage();
				}
			}
		};
	}
	
	protected Runnable initMessageSendingRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				ISendBuffer sendBuffer = initSendBuffer();
				setSendBuffer(sendBuffer);
				while (!getConnection().isClosed()) {
					if (!getSendBuffer().isEmpty() && !getSendBuffer().isBlocked()) {
						getSendBuffer().sendMessage();
					}
				}
			}
		};
	}
	
	@Override
	public void init() {
		for (Runnable r : this.initConnectionRunnables()) {
			this.es.submit(r);
		}
	}
	
	@Override
	public void sendMessage(IMessage message) {
		this.sb.addMessage(message);
	}
	
	@Override
	public void close() {
		try {
			this.sb.close();
			this.conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.iml = null;
	}
}
