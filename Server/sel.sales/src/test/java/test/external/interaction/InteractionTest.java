package test.external.interaction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import model.dish.DishMenuData;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyInteraction;
import test.external.dummy.DummyServer;
//@Disabled("Takes too long to finish")
//@Execution(value = ExecutionMode.SAME_THREAD)
class InteractionTest extends FXTestTemplate {
	private String so1;
	private String so2;
	
	private DummyInteraction interaction;
	
	private DummyServer server;
	private DummyClient client;
	
	@BeforeEach
	void prep() {
		server = new DummyServer(serviceID, serviceName, serverName, serverAddress);
		client = new DummyClient(serviceID, serviceName, clientName, clientAddress);
		
		interaction = new DummyInteraction(server, client);
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
		this.closeAll(interaction);
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	@Test
	void deviceFamiliarisingTest() {
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData().length, 1);
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceName(), clientName);
//		Assertions.assertEquals(serverModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), clientAddress);
//		
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData().length, 1);
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceName(), serverName);
//		Assertions.assertEquals(clientModel.getAllDiscoveredDeviceData()[0].getDeviceAddress(), serverAddress);
//		
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData().length, 1);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceName(), clientName);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getDeviceAddress(), clientAddress);
//		Assertions.assertEquals(serverModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
//		
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData().length, 1);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceName(), serverName);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getDeviceAddress(), serverAddress);
//		Assertions.assertEquals(clientModel.getAllKnownDeviceData()[0].getIsAllowedToConnect(), true);
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
//		clientExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
//		serverExternal.refreshKnownDevices();
//		GeneralTestUtilityClass.performWait(100);
		
		Assertions.assertEquals(client.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(client.getAllKnownDeviceData()[0].getIsConnected(), true);
		
		Assertions.assertEquals(server.getAllKnownDeviceData().length, 1);
		Assertions.assertEquals(server.getAllKnownDeviceData()[0].getIsConnected(), true);
	}
	
	@Test
	void menuExchangeTest() {
		interaction.setServerMenu(menu);
//		GeneralTestUtilityClass.performWait(100);
//		menuData = clientModel.getMenuData();
		
		DishMenuData menuData = client.getMenuData();
		while (menuData.getAllElements().size() == 0) {
			interaction.reSetServerMenu();
			GeneralTestUtilityClass.performWait(waitTime);
			menuData = client.getMenuData();
		}
		Assertions.assertTrue(server.getMenuData().equals(menuData));
	}
	
//	@Disabled
	@Test
	void orderExchangeTest() {
		interaction.broadcastMenu(menu);
		interaction.addPendingSendOrderToClient(client, o1id, so1);
		
		Assertions.assertEquals(server.getAllUnconfirmedOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(server.getAllUnconfirmedOrders()[0], server.deserialiseOrderData(so1)));
		
		Assertions.assertEquals(client.getAllSentOrders().length, 1);
		Assertions.assertTrue(this.ordersEqual(client.getAllSentOrders()[0], client.deserialiseOrderData(so1)));
	}
	
//	@Disabled
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
