package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.ConnectionListener;
import external.connection.ConnectionManager;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.incoming.IMessageReceptionist;
import external.connection.incoming.MessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.outgoing.StandardSendBuffer;
import external.connection.pingpong.IPingPong;
import external.connection.pingpong.StandardPingPong;

public class DummyConnectionManager extends ConnectionManager {

	public DummyConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es, 20000, 2000, 10);
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
				this.initPingPongRunnable(),
				this.initMessageSendingRunnable(),
				this.initMessageReadingRunnable()
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
