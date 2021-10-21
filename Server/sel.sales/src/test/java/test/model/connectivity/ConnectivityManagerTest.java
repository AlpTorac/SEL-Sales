package test.model.connectivity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import controller.MainController;
import external.client.IClient;
import model.IModel;
import model.Model;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class ConnectivityManagerTest {

	private IConnectivityManager connManager;
	private IModel model;
	
	private IClientData discoveredClient1Data;
	private IClientData discoveredClient2Data;
	
	private IClientData knownClient1Data;
	private IClientData knownClient2Data;
	
	private String client1Name;
	private String client1Address;
	private String client2Name;
	private String client2Address;
	
	@BeforeEach
	void prep() {
		client1Name = "c1n";
		client1Address = "c1a";
		client2Name = "c2n";
		client2Address = "c2a";
		discoveredClient1Data = new ClientData(client1Name,client1Address,false,false);
		discoveredClient2Data = new ClientData(client2Name,client2Address,false,false);
		knownClient1Data = new ClientData(client1Name,client1Address,true,false);
		knownClient2Data = new ClientData(client2Name,client2Address,true,false);
		initConnManager();
		connManager.addDiscoveredClient(client1Name, client1Address);
		connManager.addDiscoveredClient(client2Name, client2Address);
		connManager.addKnownClient(client1Address);
		connManager.addKnownClient(client2Address);
	}
	
	@AfterEach
	void cleanUp() {
		model.close();
	}
	
	private void initConnManager() {
		model = new Model();
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((Model) model, "connManager");
	}
	
	@Test
	void addDiscoveredClientTest() {
		initConnManager();
		connManager.addDiscoveredClient(client1Name, client1Address);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllDiscoveredClientData(), discoveredClient1Data,
				(dc1, dc2) -> {
					return dc1.getClientName().equals(dc2.getClientName()) && dc1.getClientAddress().equals(dc2.getClientAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllDiscoveredClientData(), discoveredClient2Data,
				(dc1, dc2) -> {
					return dc1.getClientName().equals(dc2.getClientName()) && dc1.getClientAddress().equals(dc2.getClientAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
	}
	
	@Test
	void addKnownClientTest() {
		initConnManager();
		connManager.addDiscoveredClient(client1Name, client1Address);
		connManager.addDiscoveredClient(client2Name, client2Address);
		connManager.addKnownClient(client1Address);
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient2Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
	}
	
	@Test
	void removeKnownClientTest() {
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient2Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		connManager.removeKnownClient(client1Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient2Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		connManager.removeKnownClient(client2Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient2Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
	}
	
	@Test
	void blockClientTest() {
		Assertions.assertTrue(connManager.isAllowedToConnect(client1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(client2Address));
		connManager.blockKnownClient(client1Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(client2Address));
		connManager.blockKnownClient(client2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
	}
	
	@Test
	void allowClientTest() {
		connManager.blockKnownClient(client1Address);
		connManager.blockKnownClient(client2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
		
		connManager.allowKnownClient(client1Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
		
		connManager.allowKnownClient(client2Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(client1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(client2Address));
	}
	
	@Test
	void requestRediscoveryTest() {
		connManager.requestClientRediscovery();
		Assertions.assertTrue(connManager.isClientRediscoveryRequested());
	}
	
	@Test
	void isConnectedTest() {
		Assertions.assertFalse(connManager.isConnected(client1Address));
		Assertions.assertFalse(connManager.isConnected(client2Address));
		
		connManager.clientConnected(client1Address);
		Assertions.assertTrue(connManager.isConnected(client1Address));
		Assertions.assertFalse(connManager.isConnected(client2Address));
		
		connManager.clientConnected(client2Address);
		Assertions.assertTrue(connManager.isConnected(client1Address));
		Assertions.assertTrue(connManager.isConnected(client2Address));
		
		connManager.clientDisconnected(client1Address);
		Assertions.assertFalse(connManager.isConnected(client1Address));
		Assertions.assertTrue(connManager.isConnected(client2Address));
		
		connManager.clientDisconnected(client2Address);
		Assertions.assertFalse(connManager.isConnected(client1Address));
		Assertions.assertFalse(connManager.isConnected(client2Address));
	}
	
	@Test
	void getKnownClientCountTest() {
		Assertions.assertEquals(2, connManager.getKnownClientCount());
		connManager.removeKnownClient(client1Address);
		Assertions.assertEquals(1, connManager.getKnownClientCount());
		connManager.removeKnownClient(client2Address);
		Assertions.assertEquals(0, connManager.getKnownClientCount());
	}
	
	@Test
	void getClientDataTest() {
		initConnManager();
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 0);
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		connManager.addDiscoveredClient(client1Name, client1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 1);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		connManager.addDiscoveredClient(client2Name, client2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		connManager.addKnownClient(client1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 1);
		Assertions.assertTrue(connManager.getAllKnownClientData()[0].equals(knownClient1Data));
		connManager.addKnownClient(client2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 2);
		Assertions.assertTrue(connManager.getAllKnownClientData()[0].equals(knownClient1Data));
		Assertions.assertTrue(connManager.getAllKnownClientData()[1].equals(knownClient2Data));
	}
}
