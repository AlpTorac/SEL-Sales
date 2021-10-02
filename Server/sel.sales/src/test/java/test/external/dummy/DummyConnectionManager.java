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
		super(controller, conn, es);
	}

	@Override
	protected ISendBuffer initSendBuffer() {
		return new StandardSendBuffer(this.getConnection(), this.getExecutorService());
	}

	@Override
	protected IMessageReceptionist initIncomingMessageListener() {
		return new MessageReceptionist(this.getConnection(), controller, this.getSendBuffer(), this.getPingPong(), this.getExecutorService());
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
	protected ConnectionListener initConnListener() {
		return new ConnectionListener(controller);
	}

	@Override
	protected DisconnectionListener initDisconListener() {
		return new DisconnectionListener(controller);
	}

	@Override
	protected IPingPong initPingPong() {
		return new StandardPingPong(this.getConnection(), this.getExecutorService());
	}

}
