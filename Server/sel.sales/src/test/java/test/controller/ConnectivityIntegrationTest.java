package test.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.IController;
import controller.MainController;
import model.IModel;
import model.Model;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import test.GeneralTestUtilityClass;

class ConnectivityIntegrationTest {

	private IModel model;
	private IController controller;
	private IConnectivityManager connManager;
	
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
		init();
		client1Name = "c1n";
		client1Address = "c1a";
		client2Name = "c2n";
		client2Address = "c2a";
		discoveredClient1Data = new ClientData(client1Name,client1Address,false,false);
		discoveredClient2Data = new ClientData(client2Name,client2Address,false,false);
		knownClient1Data = new ClientData(client1Name,client1Address,true,false);
		knownClient2Data = new ClientData(client2Name,client2Address,true,false);
		controller.getModel().addDiscoveredClient(client1Name, client1Address);
		controller.getModel().addDiscoveredClient(client2Name, client2Address);
		controller.getModel().addKnownClient(client1Address);
		controller.getModel().addKnownClient(client2Address);
	}
	
	@AfterEach
	void cleanUp() {
		
	}
	
	private void initConnManager() {
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((Model) model, "connManager");
	}
	
	private void init() {
		model = new Model();
		controller = new MainController(model);
		initConnManager();
	}
	
	@Test
	void addDiscoveredClientTest() {
		init();
		controller.getModel().addDiscoveredClient(client1Name, client1Address);
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
		init();
		controller.getModel().addDiscoveredClient(client1Name, client1Address);
		controller.getModel().addDiscoveredClient(client2Name, client2Address);
		controller.getModel().addKnownClient(client1Address);
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
		controller.getModel().removeKnownClient(client1Address);
		Assertions.assertFalse(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient1Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContains(connManager.getAllKnownClientData(), discoveredClient2Data,
				(kc, dc) -> {
					return kc.getClientName().equals(dc.getClientName()) && kc.getClientAddress().equals(dc.getClientAddress());
				}));
		controller.getModel().removeKnownClient(client2Address);
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
		controller.getModel().blockKnownClient(client1Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertTrue(connManager.isAllowedToConnect(client2Address));
		controller.getModel().blockKnownClient(client2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
	}
	
	@Test
	void allowClientTest() {
		controller.getModel().blockKnownClient(client1Address);
		controller.getModel().blockKnownClient(client2Address);
		Assertions.assertFalse(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
		
		controller.getModel().allowKnownClient(client1Address);
		Assertions.assertTrue(connManager.isAllowedToConnect(client1Address));
		Assertions.assertFalse(connManager.isAllowedToConnect(client2Address));
		
		controller.getModel().allowKnownClient(client2Address);
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
		
		controller.getModel().clientConnected(client1Address);
		Assertions.assertTrue(connManager.isConnected(client1Address));
		Assertions.assertFalse(connManager.isConnected(client2Address));
		
		controller.getModel().clientConnected(client2Address);
		Assertions.assertTrue(connManager.isConnected(client1Address));
		Assertions.assertTrue(connManager.isConnected(client2Address));
		
		controller.getModel().clientDisconnected(client1Address);
		Assertions.assertFalse(connManager.isConnected(client1Address));
		Assertions.assertTrue(connManager.isConnected(client2Address));
		
		controller.getModel().clientDisconnected(client2Address);
		Assertions.assertFalse(connManager.isConnected(client1Address));
		Assertions.assertFalse(connManager.isConnected(client2Address));
	}
	
	@Test
	void getKnownClientCountTest() {
		Assertions.assertEquals(2, connManager.getKnownClientCount());
		controller.getModel().removeKnownClient(client1Address);
		Assertions.assertEquals(1, connManager.getKnownClientCount());
		controller.getModel().removeKnownClient(client2Address);
		Assertions.assertEquals(0, connManager.getKnownClientCount());
	}
	
	@Test
	void getClientDataTest() {
		init();
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 0);
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		controller.getModel().addDiscoveredClient(client1Name, client1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 1);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		controller.getModel().addDiscoveredClient(client2Name, client2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 0);
		controller.getModel().addKnownClient(client1Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 1);
		Assertions.assertTrue(connManager.getAllKnownClientData()[0].equals(knownClient1Data));
		controller.getModel().addKnownClient(client2Address);
		Assertions.assertEquals(connManager.getAllDiscoveredClientData().length, 2);
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[0].equals(discoveredClient1Data));
		Assertions.assertTrue(connManager.getAllDiscoveredClientData()[1].equals(discoveredClient2Data));
		Assertions.assertEquals(connManager.getAllKnownClientData().length, 2);
		Assertions.assertTrue(connManager.getAllKnownClientData()[0].equals(knownClient1Data));
		Assertions.assertTrue(connManager.getAllKnownClientData()[1].equals(knownClient2Data));
	}
}
