package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.client.IClient;
import external.client.IClientManager;
import external.connection.IConnectionManager;
import external.connection.outgoing.ISendBuffer;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
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
	private long waitTime = 300;
	private DummyServiceConnectionManager serviceConnectionManager;
	
	private IClientManager manager;
	private DummyClient client1;
	private String client1Name;
	private String client1Address;
	private DummyClient client2;
	private String client2Name;
	private String client2Address;
	
	private IController controller;
	private boolean isOrderReceivedByController;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	@BeforeEach
	void prep() {
		manager = new DummyClientManager();
		client1Name = "client1Name";
		client1Address = "client1Address";
		client2Name = "client2Name";
		client2Address = "client2Address";
		client1 = new DummyClient(client1Name, client1Address);
		client2 = new DummyClient(client2Name, client2Address);
		this.discoverClients();
		manager.addClient(client1Address);
		manager.addClient(client2Address);
		manager.allowClient(client1Address);
		manager.allowClient(client2Address);
		controller = initController();
		serviceConnectionManager = new DummyServiceConnectionManager(manager, controller);
		isOrderReceivedByController = false;
	}
	
	private void discoverClients() {
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		Collection<IClient> cs = new ArrayList<IClient>();
		cs.add(client1);
		cs.add(client2);
		cds.setDiscoveredClients(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverClients();
	}
	
	@AfterEach
	void cleanUp() {
		serviceConnectionManager.close();
		isOrderReceivedByController = false;
	}
	
	private IController initController() {
		DummyController controller = new DummyController() {
			@Override
			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	@Test
	void getConnectionTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
	}
	@Test
	void acceptIncomingConnectionTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
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
		serviceConnectionManager.acceptIncomingConnection();
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
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertFalse(serviceConnectionManager.isConnectionAllowed(client2Address));
		Assertions.assertNull(serviceConnectionManager.getConnection(client2Address));
	}
	
	@Test
	void sendMessageToTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = GeneralTestUtilityClass.getPrivateFieldValue(serviceConnectionManager, "connectionManagers");
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		IMessage m = new Message(null, null, null);
		
		String targetClientAddress = client1Address;
		
		serviceConnectionManager.sendMessageTo(targetClientAddress, m);
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		// Make sure the right one gets it
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertTrue(sb.isBlocked());
			BufferUtilityClass.fillBuffer(((DummyConnection) cm.getConnection()).getInputStreamBuffer(), serialiser.serialise(m.getMinimalAcknowledgementMessage()));
		});
		
		// Make sure others do not receive it
		connectionManagers.stream().filter(cm -> !cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		connectionManagers.stream().filter(cm -> cm.getConnection().getTargetClientAddress().equals(targetClientAddress)).forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
	}
	
	@Test
	void broadcastMessageTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		serviceConnectionManager.setCurrentConnectionObject(client2);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertEquals(client2.getClientAddress(), serviceConnectionManager.getConnection(client2Address).getTargetClientAddress());
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
		
		Collection<IConnectionManager> connectionManagers = GeneralTestUtilityClass.getPrivateFieldValue(serviceConnectionManager, "connectionManagers");
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
			
			DummyConnection conn = (DummyConnection) cm.getConnection();
			BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialiser.serialise(m.getMinimalAcknowledgementMessage()));
		});
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		connectionManagers.forEach(cm -> {
			ISendBuffer sb = cm.getSendBuffer();
			Assertions.assertFalse(sb.isBlocked());
		});
	}
}
