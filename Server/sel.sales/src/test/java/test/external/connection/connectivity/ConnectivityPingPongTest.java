package test.external.connection.connectivity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controller.IController;
import external.connection.DisconnectionListener;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyServerController;
import test.external.dummy.DummyConnectivityTestWrapper;
//@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityPingPongTest {
	private ExecutorService esServer;
	private ExecutorService esDevice;
	
	private long maximalWaitTime;
	private int cyclesToWait;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser = new StandardMessageParser();
	
	private DummyConnection conn;
	
	private String DeviceName;
	private String DeviceAddress;
	
	private DummyConnectivityTestWrapper interaction;
	
	private long minimalPingPongDelay;
	private long pingPongTimeout;
	private long sendTimeout;
	private int resendLimit;
	
	private boolean isServerConnected;
	private boolean isDeviceConnected;
	
	private LocalDateTime startTime;
	
	@BeforeEach
	void prep() {
		esServer = Executors.newCachedThreadPool();
		esDevice = Executors.newCachedThreadPool();
		DeviceName = "DeviceName";
		DeviceAddress = "DeviceAddress";
		cyclesToWait = 10;
		pingPongTimeout = 1000;
		minimalPingPongDelay = 200;
		sendTimeout = 500;
		resendLimit = 50;
		
		maximalWaitTime = 10000;
		
		isServerConnected = true;
		isDeviceConnected = true;
		this.initInteraction();
		GeneralTestUtilityClass.performWait(1000);
		startTime = LocalDateTime.now();
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		isServerConnected = false;
		isDeviceConnected = false;
		startTime = null;
		try {
			esServer.awaitTermination(200, TimeUnit.MILLISECONDS);
			esDevice.awaitTermination(200, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		esServer = null;
		esDevice = null;
		GeneralTestUtilityClass.performWait(1000);
	}
	
	private void initInteraction() {
		this.interaction = new DummyConnectivityTestWrapper(esServer, esDevice, DeviceName, DeviceAddress, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay) {
			@Override
			protected IController initServerController() {
				return new DummyServerController() {
					@Override
					public void DeviceConnected(String DeviceAddress) {
						isServerConnected = true;
					}
					@Override
					public void DeviceDisconnected(String DeviceAddress) {
						isServerConnected = false;
					}
				};
			}
			@Override
			protected IController initDeviceController() {
				return new DummyServerController() {
					@Override
					public void DeviceConnected(String DeviceAddress) {
						isDeviceConnected = true;
					}
					@Override
					public void DeviceDisconnected(String DeviceAddress) {
						isDeviceConnected = false;
					}
				};
			}
			@Override
			protected DisconnectionListener initServerDisconListener() {
				return new DisconnectionListener(null) {
					@Override
					public void connectionLost(String DeviceAddress) {
						isServerConnected = false;
						try {
							if (conn != null) {
								conn.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}
			@Override
			protected DisconnectionListener initDeviceDisconListener() {
				return new DisconnectionListener(null) {
					@Override
					public void connectionLost(String DeviceAddress) {
						isDeviceConnected = false;
						try {
							if (conn != null) {
								conn.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
			}
		};
	}
	
	private boolean isServerConnected() {
		return this.isServerConnected;
	}
	
	private boolean isDeviceConnected() {
		return this.isDeviceConnected;
	}
	
	private long getTimeElapsedInMilis() {
		return startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
	}
	
	@Test
	void pingPongTest() {
		int currentCycles = 0;
		while (this.getTimeElapsedInMilis() < maximalWaitTime && currentCycles <= cyclesToWait) {
			currentCycles = this.interaction.getPingPongSuccessfulConsecutiveCycleCount();
			Assertions.assertTrue(this.isDeviceConnected());
			Assertions.assertTrue(this.isServerConnected());
		}
		Assertions.assertTrue(currentCycles >= cyclesToWait, "was: " + currentCycles + " ,expected: " + cyclesToWait);
	}
	
}
