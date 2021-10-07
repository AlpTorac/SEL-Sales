package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.connection.DisconnectionListener;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyInteraction;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityTest {
	private final Object lock = new Object();
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser = new StandardMessageParser();
	
	private DummyConnection conn;
	
	private String clientName;
	private String clientAddress;
	
	private DummyInteraction interaction;
	
	private long pingPongTimeout;
	private long sendTimeout;
	private int resendLimit;
	
	private boolean isServerConnected;
	private boolean isClientConnected;
	
	@BeforeEach
	void prep() {
		clientName = "clientName";
		clientAddress = "clientAddress";
		pingPongTimeout = 1000;
		sendTimeout = 2000;
		resendLimit = 5;
		isServerConnected = true;
		isClientConnected = true;
		this.initInteraction();
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		isServerConnected = false;
		isClientConnected = false;
	}
	
	private void initInteraction() {
		this.interaction = new DummyInteraction(clientName, clientAddress, pingPongTimeout, sendTimeout, resendLimit) {
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
	
	@Test
	void pingPongTest() {
//		synchronized (lock) {
//			boolean cond = true;
//			while (cond) {
//				cond = isServerConnected() || isClientConnected();
//			}
//		}
	}

}
