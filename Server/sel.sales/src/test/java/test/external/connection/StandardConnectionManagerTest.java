package test.external.connection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.StandardConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyServerController;
//@Execution(value = ExecutionMode.SAME_THREAD)
class StandardConnectionManagerTest {
	private ExecutorService es;
	private long waitTime = 110;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser = new StandardMessageParser();
	
	private DummyConnection conn;
	private DummyDevice Device1;
	private String Device1Name;
	private String Device1Address;
	private DummyDevice Device2;
	private String Device2Name;
	private String Device2Address;
	
	private IController controller;
	
	private StandardConnectionManager connManager;
	private ISendBuffer sb;
	private IPingPong pingPong;
	
	private int resendLimit = 10;
	
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
		Device1Name = "Device1Name";
		Device1Address = "Device1Address";
		Device2Name = "Device2Name";
		Device2Address = "Device2Address";
		Device1 = new DummyDevice(Device1Name, Device1Address);
		Device2 = new DummyDevice(Device2Name, Device2Address);
		controller = initController();
		conn = new DummyConnection(Device1Address);
		es = Executors.newCachedThreadPool();
		connManager = new StandardConnectionManager(controller, conn, es, 100, 2000, resendLimit, 0);
		connManager.start();
		sb = connManager.getSendBuffer();
		pingPong = connManager.getPingPong();
		pingPong.setDisconnectionListener(new DisconnectionListener(null) {
			@Override
			public void connectionLost(String DeviceAddress) {
				isConnected = false;
				try {
					conn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		isConnected = true;
		GeneralTestUtilityClass.performWait(waitTime);
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
		return new DummyServerController() {
			@Override
			public void DeviceConnected(String DeviceAddress) {
				isConnected = true;
			}
			@Override
			public void DeviceDisconnected(String DeviceAddress) {
				isConnected = false;
			}
		};
	}
	
	@Test
	void messageSendingTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(sb.isBlocked());
	}
	
	@Test
	void messageReadingTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(sb.isBlocked());
		IMessage incAck = m.getMinimalAcknowledgementMessage();
		ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(incAck), waitTime, 10000);
	}
	
	@Test
	void messageSendReadCycleTest() {
		Assertions.assertFalse(sb.isBlocked());
		IMessage m = new Message(null, null, null);
		connManager.sendMessage(m);
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(sb.isBlocked());
		IMessage incAck = m.getMinimalAcknowledgementMessage();
		ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(incAck), waitTime, 10000);
		
		IMessage m2 = new Message(1, null, null, null);
		connManager.sendMessage(m2);
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(sb.isBlocked());
		IMessage incAck2 = m2.getMinimalAcknowledgementMessage();
		ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(incAck2), waitTime, 10000);
	}
	
	@Test
	void pingPongTest() {
		Assertions.assertTrue(isConnected);
		int remainingResendTries = pingPong.getRemainingResendTries();
		while (remainingResendTries >= 0) {
			pingPong.sendPingPongMessage();
//			Assertions.assertEquals(remainingResendTries, pingPong.getRemainingResendTries());
			GeneralTestUtilityClass.performWait(waitTime);
			remainingResendTries--;
		}
//		Assertions.assertEquals(0, pingPong.getRemainingResendTries());
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(conn.isClosed());
	}
}
