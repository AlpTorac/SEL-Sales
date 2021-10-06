package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.connection.StandardConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyConnectionManager;
import test.external.dummy.DummyController;
@Execution(value = ExecutionMode.SAME_THREAD)
class StandardConnectionManagerTest {
	private ExecutorService es;
	private long waitTime = 100;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser = new StandardMessageParser();
	
	private DummyConnection conn;
	private DummyClient client1;
	private String client1Name;
	private String client1Address;
	private DummyClient client2;
	private String client2Name;
	private String client2Address;
	
	private IController controller;
	
	private StandardConnectionManager connManager;
	private ISendBuffer sb;
	private IPingPong pingPong;
	
	private int resendLimit;
	
	private boolean isConnected;
	
	/*
	 * Ideas to fix:
	 * 
	 * 1) Let IncomingMessageListener detect messages in the input stream (the runnable bit should be
	 * transferred to IncomingMessageListener from connection manager) (must run parallel)
	 * 
	 * 2) Make a listener for the send buffer that detects when the send buffer has a message to send and
	 * is ready (must run parallel)
	 * 
	 * 3) Make connection managers runnables, deal with incoming messages and send buffer in the run() method
	 * that has a while loop (ends when connection is closed)
	 * 
	 * 4) Initialise send buffer and incoming message listener in the constructor of connection managers,
	 * get their runnable in the run() method.
	 */
	@BeforeEach
	void prep() {
		client1Name = "client1Name";
		client1Address = "client1Address";
		client2Name = "client2Name";
		client2Address = "client2Address";
		client1 = new DummyClient(client1Name, client1Address);
		client2 = new DummyClient(client2Name, client2Address);
		controller = initController();
		conn = new DummyConnection(client1Address);
		es = Executors.newCachedThreadPool();
		isConnected = false;
		connManager = new StandardConnectionManager(controller, conn, es, 20000, 2000, 2);
		sb = connManager.getSendBuffer();
		pingPong = connManager.getPingPong();
		resendLimit = pingPong.getRemainingResendTries();
		conn.getInputStream().reset();
	}
	@AfterEach
	void cleanUp() {
		connManager.close();
		try {
			es.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
			es.shutdownNow();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isConnected = false;
	}
	
	private IController initController() {
		return new DummyController() {
			@Override
			public void clientConnected(String clientAddress) {
				isConnected = true;
			}
			@Override
			public void clientDisconnected(String clientAddress) {
				isConnected = false;
			}
		};
	}
	
	/*
	 * Use to detect ping-pong messages taking the place of normal messages.
	 */
	private boolean messageBufferContentCheck(ByteArrayOutputStream os, IMessage m) {
		IMessage message = parser.parseMessage(os.toString());
		if (!message.hasContext(MessageContext.PINGPONG)) {
			BufferUtilityClass.assertOutputWrittenContains(os, serialiser.serialise(m).getBytes());
			os.reset();
			return true;
		}
		os.reset();
		return false;
	}
	
	@Test
	void messageSendingTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
		if (!messageBufferContentCheck(conn.getOutputStream(), m)) {
			cleanUp();
			prep();
			messageSendingTest();
			return;
		}
		Assertions.assertTrue(sb.isBlocked());
	}
	
	@Test
	void messageReadingTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		ByteArrayOutputStream os = conn.getOutputStream();
		ByteArrayInputStream is = conn.getInputStream();
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
		if (!messageBufferContentCheck(os, m)) {
			cleanUp();
			prep();
			is.reset();
			messageReadingTest();
			return;
		}
		Assertions.assertTrue(sb.isBlocked());
		is.reset();
		IMessage incAck = m.getMinimalAcknowledgementMessage();
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialiser.serialise(incAck));
		GeneralTestUtilityClass.performWait(waitTime);
		//Works if sb receives acknowledgement itself
		Assertions.assertFalse(sb.isBlocked());
	}
	
	@Test
	void messageSendReadCycleTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		ByteArrayOutputStream os = conn.getOutputStream();
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
//		System.out.println("OutputStream was: " + os.toString() + "\nexpected was: " + new String(serialiser.serialise(m).getBytes()));
		if (!messageBufferContentCheck(os, m)) {
			cleanUp();
			prep();
			messageSendReadCycleTest();
			return;
		}
		Assertions.assertTrue(sb.isBlocked());
		
		IMessage incAck = m.getMinimalAcknowledgementMessage();
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialiser.serialise(incAck));
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(sb.isBlocked());
		
		IMessage m2 = new Message(null, null, null);
		os = conn.getOutputStream();
		connManager.sendMessage(m2);
		GeneralTestUtilityClass.performWait(waitTime);
		if (!messageBufferContentCheck(os, m2)) {
			cleanUp();
			prep();
			messageSendReadCycleTest();
			return;
		}
		Assertions.assertTrue(sb.isBlocked());
		
		IMessage incAck2 = m2.getMinimalAcknowledgementMessage();
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialiser.serialise(incAck2));
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(sb.isBlocked());
	}
	
	@Test
	void pingPongTest() {
		Assertions.assertTrue(isConnected);
		Assertions.assertTrue(resendLimit == pingPong.getRemainingResendTries());
		while (pingPong.getRemainingResendTries() > 0) {
			pingPong.timeout();
		}
		pingPong.timeout();
		pingPong.timeout();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(isConnected);
	}
}
