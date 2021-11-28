package test.external.pingpong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.connection.DisconnectionListener;
import external.connection.pingpong.IPingPong;
import external.message.Message;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyDevice;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyServerController;
import test.external.dummy.DummyPingPong;
//@Execution(value = ExecutionMode.SAME_THREAD)
class PingPongTest {
	private volatile boolean continueCycle = true;
	private ExecutorService es;
	private long timeoutTime = 200;
	private long waitTime = 220;
	
	private int minimumNonBlockingWaitTime = 3000;
	private int maximumNonBlockingWaitTime = 5000;
	private int maximumNonBlockingSequenceNumber = 100;
	private int maximumNonBlockingTextLength = 100;
	
	/**
	 * ONLY FOR NON BLOCKING TEST
	 */
	private int sendCount = 0;
	
	private DummyConnection conn;
	private DummyDevice Device1;
	private String Device1Name;
	private String Device1Address;
	
	private IController controller;
	
	private IPingPong pingPong;
	private int resendLimit = 10;
	
	private long minimalPingPongDelay = 100;
	
	private boolean isConnected;
	
	private boolean continueCycle() {
		return this.continueCycle;
	}
	
	@BeforeEach
	void prep() {
		Device1Name = "Device1Name";
		Device1Address = "Device1Address";
		Device1 = new DummyDevice(Device1Name, Device1Address);
		controller = initController();
		conn = new DummyConnection(Device1Address);
		es = Executors.newCachedThreadPool();
		initPingPong();
		isConnected = true;
		sendCount = 0;
	}
	
	private void initPingPong() {
		pingPong = new DummyPingPong(conn, es, minimalPingPongDelay, resendLimit, timeoutTime);
		pingPong.setDisconnectionListener(new DisconnectionListener(controller) {
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
	}
	
	@AfterEach
	void cleanUp() {
		sendCount = 0;
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
	
	@Test
	void nonBlockingTest() {
		resendLimit = 10;
		waitTime = 110;
		minimalPingPongDelay = 0;
		
		initPingPong();
		
		final Object lock = new Object();
		// Check for messages to read and count how many loops it took
		Future<Integer> cycleCount = es.submit(() -> {
			int cc = 0;
			while (continueCycle()) {
				if (pingPong.sendPingPongMessage()) {
					sendCount++;
				}
				cc++;
			}
			return cc;
		});
		
		// Wait for a random amount of time and break the cycles
		es.submit(() -> {
			synchronized (lock) {
				try {
					lock.wait(GeneralTestUtilityClass.generateRandomNumber(minimumNonBlockingWaitTime, maximumNonBlockingWaitTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continueCycle = false;
			}
		});
		
		// Wait till the runnables stop
		while (!cycleCount.isDone()) {
			
		}
		
		int cycleC = 0;
		try {
			cycleC = cycleCount.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assertions.assertTrue(cycleC > sendCount, "Cycle Count: " + cycleC + " ,sent message count: " + sendCount);
		System.out.println("In a total of " + cycleC + " cycles, " + sendCount + " messages were sent");
//		readMessages.forEach(message -> System.out.println(message));
	}
}
