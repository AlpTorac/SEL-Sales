package external.connection.connectivity;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.connection.DisconnectionListener;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyInteraction;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivitySendBufferTest {
	private ExecutorService esServer;
	private ExecutorService esClient;
	
	private long maximalWaitTime;
	private int cyclesToWait;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser = new StandardMessageParser();
	
	private DummyConnection conn;
	
	private String clientName;
	private String clientAddress;
	
	private DummyInteraction interaction;
	
	private long minimalPingPongDelay;
	private long pingPongTimeout;
	private long sendTimeout;
	private int resendLimit;
	
	private boolean isServerConnected;
	private boolean isClientConnected;
	
	private LocalDateTime startTime;
	
	@BeforeEach
	void prep() {
		esServer = Executors.newCachedThreadPool();
		esClient = Executors.newCachedThreadPool();
		clientName = "clientName";
		clientAddress = "clientAddress";
		cyclesToWait = 10;
		pingPongTimeout = 2000;
		minimalPingPongDelay = 1000;
		sendTimeout = 2000;
		resendLimit = 5;
		
		maximalWaitTime = 20000;
		
		isServerConnected = true;
		isClientConnected = true;
		this.initInteraction();
		GeneralTestUtilityClass.performWait(1000);
		startTime = LocalDateTime.now();
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		isServerConnected = false;
		isClientConnected = false;
		startTime = null;
		try {
			esServer.awaitTermination(200, TimeUnit.MILLISECONDS);
			esClient.awaitTermination(200, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		esServer = null;
		esClient = null;
		GeneralTestUtilityClass.performWait(1000);
	}
	
	private void initInteraction() {
		this.interaction = new DummyInteraction(esServer, esClient, clientName, clientAddress, pingPongTimeout, sendTimeout, resendLimit, minimalPingPongDelay) {
			@Override
			protected IController initServerController() {
				return new DummyController() {
					@Override
					public void clientConnected(String clientAddress) {
						isServerConnected = true;
					}
					@Override
					public void clientDisconnected(String clientAddress) {
						isServerConnected = false;
					}
				};
			}
			@Override
			protected IController initClientController() {
				return new DummyController() {
					@Override
					public void clientConnected(String clientAddress) {
						isClientConnected = true;
					}
					@Override
					public void clientDisconnected(String clientAddress) {
						isClientConnected = false;
					}
				};
			}
			@Override
			protected DisconnectionListener initServerDisconListener() {
				return new DisconnectionListener(null) {
					@Override
					public void connectionLost(String clientAddress) {
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
			protected DisconnectionListener initClientDisconListener() {
				return new DisconnectionListener(null) {
					@Override
					public void connectionLost(String clientAddress) {
						isClientConnected = false;
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
	
	private boolean isClientConnected() {
		return this.isClientConnected;
	}
	
	private long getTimeElapsedInMilis() {
		return startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
	}

	@Test
	void serverSendBufferTest() {
		int currentCycles = 0;
		IMessage m = new Message(null, null, null);
		while (this.getTimeElapsedInMilis() < maximalWaitTime && currentCycles < cyclesToWait) {
			this.interaction.messageToServer(m);
			currentCycles = this.interaction.getSendBufferSuccessfulCycleCount();
			Assertions.assertTrue(this.isClientConnected());
			Assertions.assertTrue(this.isServerConnected());
		}
		Assertions.assertEquals(currentCycles, cyclesToWait, "was: " + currentCycles + " ,expected: " + cyclesToWait);
	}
	
	@Test
	void clientSendBufferTest() {
		int currentCycles = 0;
		IMessage m = new Message(null, null, null);
		while (this.getTimeElapsedInMilis() < maximalWaitTime && currentCycles < cyclesToWait) {
			this.interaction.messageToClient(m);
			currentCycles = this.interaction.getSendBufferSuccessfulCycleCount();
			Assertions.assertTrue(this.isClientConnected());
			Assertions.assertTrue(this.isServerConnected());
		}
		Assertions.assertEquals(currentCycles, cyclesToWait, "was: " + currentCycles + " ,expected: " + cyclesToWait);
	}
	
	@Test
	void sendBufferCycleTest() {
		int currentCycles = 0;
		IMessage m = new Message(null, null, null);
		while (this.getTimeElapsedInMilis() < maximalWaitTime && currentCycles < cyclesToWait) {
			this.interaction.messageToServer(m);
			this.interaction.messageToClient(m);
			currentCycles = Math.max(this.interaction.getServerSendBufferSuccessfulCycleCount(), this.interaction.getClientSendBufferSuccessfulCycleCount());
			Assertions.assertTrue(this.isClientConnected());
			Assertions.assertTrue(this.isServerConnected());
		}
		Assertions.assertEquals(currentCycles, cyclesToWait, "was: " + currentCycles + " ,expected: " + cyclesToWait);
	}
}
