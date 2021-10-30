package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
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
import external.client.IClient;
import external.client.IClientManager;
import external.connection.DisconnectionListener;
import external.connection.IConnection;
import external.connection.IConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.connection.pingpong.IPingPong;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyClientManager;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyServiceConnectionManager;
@Execution(value = ExecutionMode.SAME_THREAD)
class ServiceConnectionManagerTest {
	private long waitTime = 100;
	private DummyServiceConnectionManager serviceConnectionManager;
	
	private IClientManager manager;
	private DummyClient client1;
	private String client1Name;
	private String client1Address;
	private DummyClient client2;
	private String client2Name;
	private String client2Address;
	private DummyClient client3;
	private String client3Name;
	private String client3Address;
	
	private IController controller;
	private boolean isOrderReceivedByController;
	
	private ExecutorService es;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	@BeforeEach
	void prep() {
		es = Executors.newCachedThreadPool();
		manager = new DummyClientManager(es);
		client1Name = "client1Name";
		client1Address = "client1Address";
		client2Name = "client2Name";
		client2Address = "client2Address";
		client3Name = "client3Name";
		client3Address = "client3Address";
		client1 = new DummyClient(client1Name, client1Address);
		client2 = new DummyClient(client2Name, client2Address);
		client3 = new DummyClient(client3Name, client3Address);
		this.discoverClients();
		manager.addClient(client1Address);
		manager.addClient(client2Address);
		manager.addClient(client3Address);
		manager.allowClient(client1Address);
		manager.allowClient(client2Address);
		manager.allowClient(client3Address);
		controller = initController();
		serviceConnectionManager = new DummyServiceConnectionManager(manager, controller, es);
		isOrderReceivedByController = false;
	}
	
	private void discoverClients() {
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		Collection<IClient> cs = new ArrayList<IClient>();
		cs.add(client1);
		cs.add(client2);
		cs.add(client3);
		cds.setDiscoveredClients(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverClients();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	@AfterEach
	void cleanUp() {
		serviceConnectionManager.close();
		isOrderReceivedByController = false;
		
		try {
			es.awaitTermination(waitTime/2, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private IController initController() {
		DummyController controller = new DummyController() {
//			@Override
//			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	@Test
	void getConnectionTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime * 2);
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
	}
	@Test
	void acceptIncomingConnectionTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
	}
	@Test
	void acceptIncomingUnknownConnectionTest() {
		String strangerClientName = "stranger";
		String strangerClientAddress = "fhgigdfhkigdf";
		DummyClient strangerClient = new DummyClient(strangerClientName, strangerClientAddress);
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertNull(manager.getClient(strangerClientAddress));
		Assertions.assertFalse(manager.isAllowedToConnect(strangerClientAddress));
		serviceConnectionManager.setCurrentConnectionObject(strangerClient);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(serviceConnectionManager.isConnectionAllowed(strangerClientAddress));
		Assertions.assertNull(serviceConnectionManager.getConnection(strangerClientAddress));
	}
	@Test
	void acceptIncomingBlockedConnectionTest() {
		manager.blockClient(client2Address);
		Assertions.assertEquals(client2.getClientAddress(),manager.getClient(client2Address).getClientAddress());
		Assertions.assertFalse(manager.isAllowedToConnect(client2Address));
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(serviceConnectionManager.isConnectionAllowed(client2Address));
		Assertions.assertNull(serviceConnectionManager.getConnection(client2Address));
	}
	
	@Test
	void sendMessageToTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = serviceConnectionManager.getConnectionManagers();
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		IMessage m = new Message(null, null, null);
		
		String targetClientAddress = client1Address;
		
		serviceConnectionManager.sendMessageTo(targetClientAddress, m);
		
		GeneralTestUtilityClass.performWait(waitTime*2);
		
		// Make sure the right one gets it
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
			
			DummyConnection conn = (DummyConnection) cm.getConnection();
			conn.fillInputBuffer(serialiser.serialise(m.getMinimalAcknowledgementMessage()));
		});
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		// Make sure others do not receive it
		connectionManagers.stream().filter(cm -> !cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			DummyConnection conn = (DummyConnection) cm.getConnection();
			
			ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(m.getMinimalAcknowledgementMessage()), waitTime, 10000);
		});
	}
	
	@Test
	void broadcastMessageTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = serviceConnectionManager.getConnectionManagers();
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		IMessage m = new Message(null, null, null);
		serviceConnectionManager.broadcastMessage(m);
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
		});
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			DummyConnection conn = (DummyConnection) cm.getConnection();
			ConnectionManagerTestUtilityClass.assertAckReadAndSentToSendBuffer(conn, sb, serialiser.serialise(m.getMinimalAcknowledgementMessage()), waitTime, 10000);
		});
	}
	
	private volatile boolean isDisconnected = false;
	
	@Test
	void disconnectionViaWaitingTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = serviceConnectionManager.getConnectionManagers();
		
		GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT / 2);
		IConnectionManager cm = connectionManagers.stream().findAny().get();
		IPingPong pp = cm.getPingPong();
		pp.receiveResponse(new Message(MessageContext.PINGPONG, null, null));
		IConnectionManager cm2 = connectionManagers.stream().filter(man -> man != cm).findAny().get();
		DisconnectionListener l = new DisconnectionListener(null) {
			@Override
			public void fireApplicationEvent(IController controller) {
				isDisconnected = true;
			}
		};
		cm2.setDisconnectionListener(l);
		GeneralTestUtilityClass.performWait(DummyServiceConnectionManager.ESTIMATED_PP_TIMEOUT / 2);
		Assertions.assertTrue(isDisconnected);
	}
	
	@Test
	void disconnectionViaDataTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		serviceConnectionManager.setCurrentConnectionObject(client3);
//		serviceConnectionManager.makeNewConnectionThread();
		GeneralTestUtilityClass.performWait(waitTime*2);
		Assertions.assertEquals(client3.getClientAddress(), serviceConnectionManager.getConnection(client3Address).getTargetClientAddress());
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = serviceConnectionManager.getConnectionManagers();
		IConnectionManager cm = connectionManagers.stream().filter(man -> man.getConnection().getTargetClientAddress().equals(client1Address)).findFirst().get();
		IConnectionManager cm2 = connectionManagers.stream().filter(man -> man.getConnection().getTargetClientAddress().equals(client2Address)).findFirst().get();
		IConnectionManager cm3 = connectionManagers.stream().filter(man -> man.getConnection().getTargetClientAddress().equals(client3Address)).findFirst().get();
		
		DisconnectionListener l = new DisconnectionListener(null) {
			@Override
			public void fireApplicationEvent(IController controller) {
				isDisconnected = true;
			}
		};
		
		serviceConnectionManager.setDisconnectionListener(l);
		
		serviceConnectionManager.receiveKnownClientData(new IClientData[] {
				new ClientData(client1Name, client1Address, false, true),
				new ClientData(client2Name, client2Address, true, false),
				new ClientData(client3Name, client3Address, false, false)
		});
		
		Assertions.assertTrue(isDisconnected);
		Assertions.assertTrue(cm.isClosed());
		Assertions.assertTrue(cm2.isClosed());
		Assertions.assertTrue(cm3.isClosed());
	}
}
