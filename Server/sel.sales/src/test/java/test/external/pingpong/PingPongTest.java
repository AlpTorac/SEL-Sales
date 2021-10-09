package test.external.pingpong;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import external.connection.DisconnectionListener;
import external.connection.StandardConnectionManager;
import external.connection.outgoing.BasicMessageSender;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.connection.pingpong.StandardPingPong;
import external.connection.timeout.FixTimeoutStrategy;
import external.connection.timeout.ITimeoutStrategy;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyPingPong;
@Execution(value = ExecutionMode.SAME_THREAD)
class PingPongTest {
	private ExecutorService es;
	private long timeoutTime = 100;
	private long waitTime = 110;
	
	private DummyConnection conn;
	private DummyClient client1;
	private String client1Name;
	private String client1Address;
	
	private IController controller;
	
	private IPingPong pingPong;
	private int resendLimit = 10;
	
	private long minimalPingPongDelay = 0;
	
	private boolean isConnected;
	
	@BeforeEach
	void prep() {
		client1Name = "client1Name";
		client1Address = "client1Address";
		client1 = new DummyClient(client1Name, client1Address);
		controller = initController();
		conn = new DummyConnection(client1Address);
		es = Executors.newCachedThreadPool();
		pingPong = new DummyPingPong(conn, es, minimalPingPongDelay, resendLimit, timeoutTime);
		pingPong.setDisconnectionListener(new DisconnectionListener(controller) {
			@Override
			public void connectionLost(String clientAddress) {
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
	}
	@AfterEach
	void cleanUp() {
		try {
			pingPong.close();
			conn.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			es.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	@Test
	void resendCountResetTest() {
		Assertions.assertEquals(resendLimit, pingPong.getRemainingResendTries());
		int remainingResendTries = resendLimit;
		while (remainingResendTries >= 0) {
			pingPong.sendPingPongMessage();
			Assertions.assertEquals(remainingResendTries, pingPong.getRemainingResendTries());
			GeneralTestUtilityClass.performWait(waitTime);
			remainingResendTries--;
		}
		Assertions.assertEquals(0, pingPong.getRemainingResendTries());
		pingPong.receiveResponse(new Message(null, null, null));
		Assertions.assertEquals(resendLimit, pingPong.getRemainingResendTries());
		Assertions.assertTrue(conn.isClosed());
	}
	
	@Test
	void disconnectTest() {
		int remainingResendTries = resendLimit;
		while (remainingResendTries >= 0) {
			pingPong.sendPingPongMessage();
			Assertions.assertEquals(remainingResendTries, pingPong.getRemainingResendTries());
			GeneralTestUtilityClass.performWait(waitTime);
			remainingResendTries--;
		}
		Assertions.assertEquals(0, pingPong.getRemainingResendTries());
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertTrue(conn.isClosed());
	}

	@Test
	void recoveryTest() {
		int cyclesToWait = 20;
		ArrayList<Boolean> cycleStatuses = new ArrayList<Boolean>();
		boolean cycleStatus = false;
		int resendLimit = 20;
		int resendCount = resendLimit;
		pingPong = new DummyPingPong(conn, es, minimalPingPongDelay, resendLimit, timeoutTime);
		Assertions.assertFalse(pingPong.isBlocked());
		Assertions.assertFalse(pingPong.isClosed());
		Assertions.assertFalse(pingPong.hasRunningTimer());
		for (int successfulCycles = 0; successfulCycles < cyclesToWait;) {
			if (GeneralTestUtilityClass.generateRandomNumber(1, 5) < 3) {
				cycleStatus = false;
			} else {
				cycleStatus = true;
			}
//			while (pingPong.hasRunningTimer()) {
//				pingPong.sendPingPongMessage();
//			}
			if (!pingPong.isBlocked()) {
				while (pingPong.hasRunningTimer()) {
					
				}
				pingPong.sendPingPongMessage();
			}
			cycleStatuses.add(cycleStatus);
			if (cycleStatuses.size() == 0 || cycleStatus) {
				GeneralTestUtilityClass.performWait(timeoutTime / 2);
				pingPong.receiveResponse(new Message(null, null, null));
				Assertions.assertFalse(pingPong.isBlocked());
				resendCount = resendLimit;
				Assertions.assertEquals(resendCount, pingPong.getRemainingResendTries());
				successfulCycles++;
			} else {
				pingPong.sendPingPongMessage();
				Assertions.assertEquals(resendCount, pingPong.getRemainingResendTries());
				GeneralTestUtilityClass.performWait(waitTime);
				resendCount--;
				Assertions.assertTrue(pingPong.isBlocked());
			}
		}
		System.out.print("Successful with: " + "[");
		for (int i = 0; i < cycleStatuses.size() - 1; i++) {
			System.out.print(cycleStatuses.get(i) + ",");
		}
		System.out.println(cycleStatuses.get(cycleStatuses.size() - 1) + "]");
	}
	
}
