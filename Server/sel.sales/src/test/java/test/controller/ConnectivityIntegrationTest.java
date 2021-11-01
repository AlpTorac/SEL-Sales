package test.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.BusinessEvent;
import controller.IController;
import controller.MainController;
import model.IModel;
import model.Model;
import model.connectivity.ClientData;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import model.order.IOrderData;
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
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private String i3id = "item3";
	
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
		model.close();
	}
	
	private void initConnManager() {
		connManager = GeneralTestUtilityClass.getPrivateFieldValue((Model) model, "connManager");
	}
	
	private void init() {
		model = new Model();
		controller = new MainController(model);
		
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		
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
	
	@Test
	void addOrdersViaController() {
		Assertions.assertEquals(0, model.getAllUnconfirmedOrders().length);
		Assertions.assertEquals(0, model.getAllConfirmedOrders().length);
		
		String serialisedOrder1 = "order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1;";
		String serialisedOrder2 = "order6#20200813000000183#1#1:item3,5;item3,4;";
		String serialisedOrder3 = "order7#20200909112233937#0#0:item1,2;item2,5;";
		
		controller.handleApplicationEvent(BusinessEvent.ADD_ORDER, new Object[] {serialisedOrder1});
		controller.handleApplicationEvent(BusinessEvent.ADD_ORDER, new Object[] {serialisedOrder2});
		controller.handleApplicationEvent(BusinessEvent.ADD_ORDER, new Object[] {serialisedOrder3});
		
		Assertions.assertEquals(3, model.getAllUnconfirmedOrders().length);
		Assertions.assertEquals(0, model.getAllConfirmedOrders().length);
		
		IOrderData[] orders = model.getOrderHelper().deserialiseOrderDatas(
				serialisedOrder1 + System.lineSeparator() + 
				serialisedOrder2 + System.lineSeparator() +
				serialisedOrder3 + System.lineSeparator()
				);
		
		GeneralTestUtilityClass.arrayContentEquals(model.getAllUnconfirmedOrders(), orders);
	}
}
