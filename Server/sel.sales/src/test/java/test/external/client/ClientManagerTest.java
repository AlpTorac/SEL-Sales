package test.external.client;

import static org.junit.jupiter.api.Assertions.*;

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

import external.client.ClientDiscoveryStrategy;
import external.client.ClientManager;
import external.client.IClient;
import external.client.IClientManager;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyClientDiscoveryStrategy;
import test.external.dummy.DummyClientManager;
@Execution(value = ExecutionMode.SAME_THREAD)
class ClientManagerTest {
	private String client1Name;
	private String client1Address;
	private String client2Name;
	private String client2Address;
	private IClient client1;
	private IClient client2;
	private IClientManager manager;
	private ExecutorService es;
	
	@BeforeEach
	void prep() {
		this.es = Executors.newCachedThreadPool();
		this.client1Name = "client1";
		this.client1Address = "client1Address";
		this.client2Name = "client2";
		this.client2Address = "client2Address";
		this.client1 = new DummyClient(this.client1Name, this.client1Address);
		this.client2 = new DummyClient(this.client2Name, this.client2Address);
		this.manager = new DummyClientManager(es);
		this.discoverClients();
		manager.addClient(client1Address);
		manager.addClient(client2Address);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			this.es.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void discoverClients() {
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		Collection<IClient> cs = new ArrayList<IClient>();
		cs.add(client1);
		cs.add(client2);
		cds.setDiscoveredClients(cs);
		this.manager.setDiscoveryStrategy(cds);
		this.manager.discoverClients();
		GeneralTestUtilityClass.performWait(300);
	}
	
	@Test
	void getClientTest() {
		Assertions.assertTrue(client1.equals(manager.getClient(client1.getClientAddress())));
		Assertions.assertTrue(client2.equals(manager.getClient(client2.getClientAddress())));
		Assertions.assertNull(manager.getClient("fkjhgsdfhggsdfkhg"));
	}
	
	@Test
	void addClientTest() {
		this.manager = new DummyClientManager(es);
		this.discoverClients();
		Assertions.assertEquals(0, manager.getClientCount());
		manager.addClient(client1Address);
		Assertions.assertEquals(1, manager.getClientCount());
		Assertions.assertTrue(client1.equals(manager.getClient(client1.getClientAddress())));
		
		Assertions.assertEquals(1, manager.getClientCount());
		manager.addClient(client2Address);
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
		this.manager = new DummyClientManager(es);
		Collection<IClient> clientCol = new ArrayList<IClient>();
		clientCol.add(client1);
		clientCol.add(client2);
		DummyClientDiscoveryStrategy cds = new DummyClientDiscoveryStrategy();
		cds.setDiscoveredClients(clientCol);
		manager.setDiscoveryStrategy(cds);
		manager.discoverClients();
		GeneralTestUtilityClass.performWait(300);
		Assertions.assertTrue(manager.getDiscoveredClients().containsAll(clientCol));
		Assertions.assertFalse(manager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(manager.isAllowedToConnect(client2Address));
	}
}
