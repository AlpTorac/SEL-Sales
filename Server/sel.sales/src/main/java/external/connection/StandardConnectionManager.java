package external.connection;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;

public class StandardConnectionManager extends ConnectionManager {

	public StandardConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es);
	}

	@Override
	protected ISendBuffer initSendBuffer() {
		return new StandardSendBuffer(this.getConnection(), this.getExecutorService());
	}

	@Override
	protected IMessageReceptionist initIncomingMessageListener() {
		return new MessageReceptionist(this.getConnection(),
				controller,
				this.getSendBuffer(), this.getExecutorService());
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
