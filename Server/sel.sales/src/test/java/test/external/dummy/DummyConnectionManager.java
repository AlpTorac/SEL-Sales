package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.connection.ConnectionManager;
import external.connection.IConnection;
import external.connection.IIncomingMessageListener;
import external.connection.IncomingMessageListener;

public class DummyConnectionManager extends ConnectionManager {

	DummyConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es);
	}

	@Override
	protected ISendBuffer initSendBuffer() {
		return new StandardSendBuffer(this.getConnection().getOutputStream(), this.getExecutorService());
	}

	@Override
	protected IIncomingMessageListener initIncomingMessageListener() {
		return new IncomingMessageListener(this.getConnection().getInputStream(), this.getConnection().getOutputStream(), controller, this.getSendBuffer());
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
