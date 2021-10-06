package external.connection;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.incoming.MessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.outgoing.StandardSendBuffer;
import external.connection.pingpong.IPingPong;
import external.connection.pingpong.StandardPingPong;

public class StandardConnectionManager extends ConnectionManager {

	public StandardConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es);
	}
	
	public StandardConnectionManager(IController controller, IConnection conn, ExecutorService es, long pingPongTimeoutInMillis, long sendTimeoutInMillis, int resendLimit) {
		super(controller, conn, es, pingPongTimeoutInMillis, sendTimeoutInMillis, resendLimit);
	}

	@Override
	protected ISendBuffer createSendBuffer(long timeoutInMillis) {
		return new StandardSendBuffer(this.getConnection(), this.getExecutorService(), timeoutInMillis);
	}

	@Override
	protected IMessageReceptionist createMessageReceptionist(ISendBuffer sb, IPingPong pingPong) {
		return new MessageReceptionist(this.getConnection(),
				controller,
				sb, pingPong, this.getExecutorService());
	}

	@Override
	protected Runnable[] initConnectionRunnables() {
		Runnable[] rs = new Runnable[] {
				this.initMessageReadingRunnable(),
				this.initMessageSendingRunnable(),
				this.initPingPongRunnable()
		};
		return rs;
	}

	@Override
	protected ConnectionListener createConnListener() {
		return new ConnectionListener(controller);
	}

	@Override
	protected DisconnectionListener createDisconListener() {
		return new DisconnectionListener(controller);
	}

	@Override
	protected IPingPong createPingPong(int resendLimit, long pingPongTimeout) {
		return new StandardPingPong(this.getConnection(), this.getExecutorService(), resendLimit, pingPongTimeout);
	}
}
