package test.external.interaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.dish.DishMenuData;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnectionUtility;
import test.external.dummy.DummyStandardClient;
import test.external.dummy.DummyStandardInteraction;
import test.external.dummy.DummyStandardServer;

//@Disabled("Takes too long to finish")
class StandardExternalInteractionTest extends FXTestTemplate {
	private String so1;
	private String so2;
	
	private DummyStandardServer server;
	private DummyConnectionUtility connUtilServer;
	
	private DummyStandardClient client;
	private DummyConnectionUtility connUtilClient;
	
	private DummyStandardInteraction interaction;
	
	@BeforeEach
	void prep() {
		this.connUtilServer = new DummyConnectionUtility(this.serverAddress);
		this.connUtilClient = new DummyConnectionUtility(this.clientAddress);
		
		server = new DummyStandardServer(serverName, serverAddress, connUtilServer);
		client = new DummyStandardClient(clientName, clientAddress, connUtilClient);
		
		interaction = new DummyStandardInteraction(server, client);
		interaction.connectPartakers(server, client);
		
		this.initDishMenuItems(server.getModel());
		this.initDishMenu();
		this.initOrders(client.getModel());
		this.getPrivateFieldsFromModel(client.getModel());
		so1 = orderDAO.serialiseValueObject(oData1);
		so2 = orderDAO.serialiseValueObject(oData2);
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	@Test
	void deviceFamiliarisingTest() {
		Assertions.assertEquals(server.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(server.getAllDiscoveredDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(server.getAllDiscoveredDeviceData()[0].getDeviceAddress(), clientAddress);
		
		Assertions.assertEquals(client.getAllDiscoveredDeviceData().length, 1);
		Assertions.assertEquals(client.getAllDiscoveredDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(client.getAllDiscoveredDeviceData()[0].getDeviceAddress(), serverAddress);
		
		Assertions.assertEquals(server.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getDeviceName(), clientName);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getDeviceAddress(), clientAddress);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
		
		Assertions.assertEquals(client.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getDeviceName(), serverName);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getDeviceAddress(), serverAddress);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
	}
	
	@Test
	void connectionTest() {
		Assertions.assertEquals(client.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getIsConnected(), true);
		
		Assertions.assertEquals(server.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getIsConnected(), true);
	}
	
	@Test
	void menuExchangeTest() {
		interaction.setServerMenu(menu);
		
		DishMenuData menuData = client.getMenuData();
		while (menuData.getAllElements().size() == 0) {
			interaction.reSetServerMenu();
			GeneralTestUtilityClass.performWait(waitTime);
			menuData = client.getMenuData();
		}
		Assertions.assertTrue(server.getMenuData().equals(menuData));
	}
	
	@Test
	void orderExchangeTest() {
		interaction.broadcastMenu(menu);
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(server.getAllUnconfirmedOrders()[0], server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(client.getAllSentOrders()[0], client.deserialiseOrderData(so1)));
	}
	
	@Test
	void multipleOrderExchangeTest() {
		interaction.broadcastMenu(menu);
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(server.getAllUnconfirmedOrders()[0], server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(client.getAllSentOrders()[0], client.deserialiseOrderData(so1)));
		
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(server.getAllUnconfirmedOrders()[0], server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(client.getAllSentOrders()[0], client.deserialiseOrderData(so1)));
		
		interaction.addPendingSendOrderToClient(client, o2id, so2);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 2);
		Assertions.assertTrue(this.ordersEqual(server.getOrder(o2id), server.deserialiseOrderData(so2)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 2);
		Assertions.assertTrue(this.ordersEqual(client.getOrder(o2id), client.deserialiseOrderData(so2)));
	}
	
	@Test
	void menuUpdateTest() {
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.removeElement(i1id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.removeElement(i2id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
		
		menu.addElement(iData1);
		menu.addElement(iData2);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		Assertions.assertTrue(client.menuDatasEqual(server));
	}
	
	@Test
	void tableNumberUpdateTest() {
		interaction.broadcastTableNumbers(serialisedTableNumbers);
		Assertions.assertTrue(server.tableNumbersEqual(client));
		
		interaction.broadcastTableNumbers(serialisedTableNumbers2);
		Assertions.assertTrue(server.tableNumbersEqual(client));
	}
}
