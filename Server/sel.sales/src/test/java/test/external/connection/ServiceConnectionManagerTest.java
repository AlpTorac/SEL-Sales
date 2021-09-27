package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.IController;
import external.client.IClient;
import external.client.IClientManager;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientManager;
import test.external.dummy.DummyController;
import test.external.dummy.DummyServiceConnectionManager;

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
	
	void prep() {
		manager = new DummyClientManager();
		client1Name = "client1Name";
		client1Address = "client1Address";
		client2Name = "client2Name";
		client2Address = "client2Address";
		client1 = new DummyClient(client1Name, client1Address);
		client2 = new DummyClient(client2Name, client2Address);
		manager.addClient(client1);
		manager.addClient(client2);
		controller = initController();
		serviceConnectionManager = new DummyServiceConnectionManager(manager, controller);
		isOrderReceivedByController = false;
	}
	
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
	void mainTest() {
		prep();
		getConnectionTest();
		cleanUp();
		
		prep();
		acceptIncomingConnectionTest();
		cleanUp();
		
		prep();
		acceptIncomingUnknownConnectionTest();
		cleanUp();
		
		prep();
		acceptIncomingBlockedConnectionTest();
		cleanUp();
	}
	
	void getConnectionTest() {
		serviceConnectionManager.setCurrentConnectionObject(client1);
		serviceConnectionManager.acceptIncomingConnection();
		GeneralTestUtilityClass.performWait(waitTime);
		Assertions.assertEquals(client1.getClientAddress(), serviceConnectionManager.getConnection(client1Address).getTargetClientAddress());
	}
	
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
	
}
