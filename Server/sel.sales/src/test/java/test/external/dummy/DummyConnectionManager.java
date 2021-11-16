package test.external.dummy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import controller.IController;
import external.connection.IConnection;
import external.connection.StandardConnectionManager;
import external.connection.incoming.IMessageReceptionist;
import external.connection.outgoing.ISendBuffer;
import external.connection.outgoing.StandardSendBuffer;
import external.connection.pingpong.IPingPong;
import external.connection.pingpong.StandardPingPong;
import server.external.connection.incoming.ServerMessageReceptionist;

public class DummyConnectionManager extends StandardConnectionManager {

	private Collection<String> receivedMessages = new ArrayList<String>();
	
	private int pingPongSuccessfulConsecutiveCycleCount = 0;
	private int pingPongSuccessfulCycleCount = 0;
	
	private int sendBufferSuccessfulConsecutiveCycleCount = 0;
	private int sendBufferSuccessfulCycleCount = 0;
	
	public DummyConnectionManager(IController controller, IConnection conn, ExecutorService es) {
		super(controller, conn, es, 20000, 2000, 10, 1000);
	}

	public DummyConnectionManager(IController controller, IConnection conn, ExecutorService es, long pingPongTimeout,
			long sendTimeout, int resendLimit, long minimalPingPongDelay) {
		super(controller, conn, es, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay);
	}
	
	@Override
	protected ISendBuffer createSendBuffer(long timeoutInMillis) {
		return new StandardSendBuffer(this.getConnection(), this.getExecutorService(), timeoutInMillis) {
			@Override
			protected boolean resendLast() {
				sendBufferSuccessfulConsecutiveCycleCount = 0;
				return super.resendLast();
			}
			@Override
			protected void unblock() {
				super.unblock();
				sendBufferSuccessfulCycleCount++;
				sendBufferSuccessfulConsecutiveCycleCount++;
			}
		};
	}
	
	@Override
	protected IPingPong createPingPong(long minimalDelay, int resendLimit, long pingPongTimeout) {
		return new StandardPingPong(this.getConnection(), this.getExecutorService(), minimalDelay, resendLimit, pingPongTimeout) {
			@Override
			protected void increaseResendCount() {
				super.increaseResendCount();
				pingPongSuccessfulConsecutiveCycleCount = 0;
			}
			
			@Override
			protected void unblock() {
				super.unblock();
				pingPongSuccessfulCycleCount++;
				pingPongSuccessfulConsecutiveCycleCount++;
			}
		};
	}
	
	@Override
	protected IMessageReceptionist createMessageReceptionist(ISendBuffer sb, IPingPong pingPong) {
		return new ServerMessageReceptionist(this.getConnection(),
				controller,
				sb, pingPong, this.getExecutorService()) {
			@Override
			protected String[] readMessages() {
				String[] readMessages = super.readMessages();
				if (readMessages != null) {
					for (String m : readMessages) {
						System.out.println("Received message: " + m);
						receivedMessages.add(m);
					}
				}
				return readMessages;
			}
		};
	}
	
//	@Override
//	protected void sendPingPongMessage() {
//		
//	}
	
	public int getPingPongSuccessfulConsecutiveCycleCount() {
		return pingPongSuccessfulConsecutiveCycleCount;
	}

	public int getPingPongSuccessfulCycleCount() {
		return pingPongSuccessfulCycleCount;
	}
	
	public int getSendBufferSuccessfulConsecutiveCycleCount() {
		return sendBufferSuccessfulConsecutiveCycleCount;
	}

	public int getSendBufferSuccessfulCycleCount() {
		return sendBufferSuccessfulCycleCount;
	}

	public int getPingPongResendCount() {
		return this.getPingPongResendLimit() - this.getPingPong().getRemainingResendTries();
	}
	
	public boolean isMessageReceived(String serialisedMessage) {
		return this.receivedMessages.contains(serialisedMessage);
	}
}
