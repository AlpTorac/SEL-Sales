package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.connection.ConnectionManager;
import external.connection.IConnection;
import external.connection.IMessageReceptionist;
import external.connection.MessageReceptionist;

public class DummyConnectionManager extends ConnectionManager {

	public DummyConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es);
	}

	@Override
	protected ISendBuffer initSendBuffer() {
		return new StandardSendBuffer(this.getConnection(), this.getExecutorService());
	}

	@Override
	protected IMessageReceptionist initIncomingMessageListener() {
		return new MessageReceptionist(this.getConnection(), controller, this.getSendBuffer(), this.getExecutorService());
	}

	@Override
	protected Runnable[] initConnectionRunnables() {
		Runnable[] rs = new Runnable[] {
				this.initMessageReadingRunnable(),
				this.initMessageSendingRunnable()
		};
		return rs;
	}

}
