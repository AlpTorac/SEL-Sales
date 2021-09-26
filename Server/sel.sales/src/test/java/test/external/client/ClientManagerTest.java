package test.external.client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;
import external.client.IClient;
import external.client.IClientManager;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyClientManager;

class ClientManagerTest {
	private String client1Name;
	private String client1Address;
	private String client2Name;
	private String client2Address;
	private IClient client1;
	private IClient client2;
	private IClientManager manager;
	
	@BeforeEach
	void prep() {
		this.client1Name = "client1";
		this.client1Address = "client1Address";
		this.client2Name = "client2";
		this.client2Address = "client2Address";
		this.client1 = new DummyClient(this.client1Name, this.client1Address);
		this.client2 = new DummyClient(this.client2Name, this.client2Address);
		this.manager = new DummyClientManager();
		manager.addClient(client1);
		manager.addClient(client2);
	}
	
	@Test
	void getClientTest() {
		Assertions.assertTrue(client1.equals(manager.getClient(client1.getClientAddress())));
		Assertions.assertTrue(client2.equals(manager.getClient(client2.getClientAddress())));
		Assertions.assertNull(manager.getClient("fkjhgsdfhggsdfkhg"));
	}
	
	@Test
	void addClientTest() {
		this.manager = new DummyClientManager();
		Assertions.assertEquals(0, manager.getClientCount());
		manager.addClient(client1);
		Assertions.assertEquals(1, manager.getClientCount());
		Assertions.assertTrue(client1.equals(manager.getClient(client1.getClientAddress())));
		
		Assertions.assertEquals(1, manager.getClientCount());
		manager.addClient(client2);
		Assertions.assertEquals(2, manager.getClientCount());
		Assertions.assertTrue(client1.equals(manager.getClient(client1.getClientAddress())));
		Assertions.assertTrue(client2.equals(manager.getClient(client2.getClientAddress())));
	}
	
	@Test
	void blockClientTest() {
		Assertions.assertTrue(manager.isAllowedToConnect(client1Address));
		manager.blockClient(client1Address);
		Assertions.assertFalse(manager.isAllowedToConnect(client1Address));
	}
	
	@Test
	void allowClientTest() {
		Assertions.assertTrue(manager.isAllowedToConnect(client1Address));
		manager.blockClient(client1Address);
		Assertions.assertFalse(manager.isAllowedToConnect(client1Address));
		manager.allowClient(client1Address);
		Assertions.assertTrue(manager.isAllowedToConnect(client1Address));
	}
	
	@Test
	void removeClientTest() {
		Assertions.assertEquals(2, manager.getClientCount());
		manager.removeClient(client1Address);
		Assertions.assertEquals(1, manager.getClientCount());
		
		manager.removeClient("gflkjhgdfshkjgsdfkjlh");
		
		Assertions.assertEquals(1, manager.getClientCount());
		manager.removeClient(client2Address);
		Assertions.assertEquals(0, manager.getClientCount());
	}
	
	@Test
	void discoverClientsTest() {
		this.manager = new DummyClientManager();
		Collection<IClient> clientCol = new ArrayList<IClient>();
		clientCol.add(client1);
		clientCol.add(client2);
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		cds.setDiscoveredClients(clientCol);
		manager.setDiscoveryStrategy(cds);
		Assertions.assertTrue(manager.discoverClients().containsAll(clientCol));
	}
}
