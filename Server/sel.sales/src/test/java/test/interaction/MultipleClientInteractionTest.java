package test.interaction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import model.connectivity.DeviceData;
import model.connectivity.IDeviceData;
import model.dish.DishMenu;
import model.dish.DishMenuData;
import model.order.OrderData;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyClient;
import test.external.dummy.DummyInteraction;
import test.external.dummy.DummyServer;

@Disabled("Takes too long to finish")
//@Execution(value = ExecutionMode.SAME_THREAD)
class MultipleClientInteractionTest {
	private long waitTime = 100;
	
	private String client1Address = "client1Address";
	private String client1Name = "client1Name";
	
	private String client2Address = "client2Address";
	private String client2Name = "client2Name";
	
	private String client3Address = "client3Address";
	private String client3Name = "client3Name";
	
	private String[] clientNames = new String[] {client1Name, client2Name, client3Name};
	private String[] clientAddresses = new String[] {client1Address, client2Address, client3Address};
	
	private String serverAddress = "serverAddress";
	private String serverName = "serverName";
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private BigDecimal o1a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a1 = BigDecimal.valueOf(2);
	private BigDecimal o2a2 = BigDecimal.valueOf(3);
	private BigDecimal o3a3 = BigDecimal.valueOf(5);
	
	private String o1id = "order1";
	private String o2id = "order2";
	private String o3id = "order3";
	private String[] oids = new String[] {o1id, o2id, o3id};
	
	private String so1;
	private String so2;
	private String so3;
	private String[] sos;
	
	private String so1Body;
	private String so2Body;
	private String so3Body;
	private String[] soBodys;
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	private String serviceID = "serviceID";
	private String serviceName = "serviceName";
	
	private DishMenu menu;
	
	private DummyInteraction interaction;
	
	private DummyServer server;
	private DummyClient[] clients;
	
	private DummyClient client1;
	private DummyClient client2;
	private DummyClient client3;
	
	@BeforeEach
	void prep() {
		server = new DummyServer(serviceID, serviceName, serverName, serverAddress);
		client1 = new DummyClient(serviceID, serviceName, client1Name, client1Address);
		client2 = new DummyClient(serviceID, serviceName, client2Name, client2Address);
		client3 = new DummyClient(serviceID, serviceName, client3Name, client3Address);
		clients = new DummyClient[] {client1, client2, client3};
		
		interaction = new DummyInteraction(server, clients);
		for (DummyClient dc : clients) {
			interaction.connectPartakers(server, dc);
		}
		
		menu = server.createDishMenu();
		menu.addMenuItem(server.createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1id));
		menu.addMenuItem(server.createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2id));
		menu.addMenuItem(server.createDishMenuItem(i3Name, i3PorSize, i3ProCost, i3Price, i3id));
		
		so1Body = "#20200809235959866#1#0:item1,2;item2,3;";
		so2Body = "#20200809235959866#1#0:item1,2;item2,3;item3,5;";
		so3Body = "#20200809235959866#1#0:item2,3;item3,5;";
		soBodys = new String[] {so1Body, so2Body, so3Body};
		
		so1 = o1id+so1Body;
		so2 = o2id+so2Body;
		so3 = o3id+so3Body;
		sos = new String[] {so1, so2, so3};
	}
	
	@AfterEach
	void cleanUp() {
		interaction.close();
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	@Test
	void connectionTest() {
		Assertions.assertEquals(server.getAllKnownDeviceData().length, clients.length);
		
		for (int i = 0; i < clients.length; i++) {
			Assertions.assertEquals(server.getAllKnownDeviceData()[i].getIsConnected(), true);
		}
		
		for (DummyClient dc : clients) {
			Assertions.assertEquals(dc.getAllKnownDeviceData().length, 1);
			Assertions.assertEquals(dc.getAllKnownDeviceData()[0].getIsConnected(), true);
		}
		
		for (DummyClient dcOut : clients) {
			for (DummyClient dcIn : clients) {
				Assertions.assertNull(dcOut.getConnection(dcIn.getAddress()));
				Assertions.assertNull(dcIn.getConnection(dcOut.getAddress()));
			}
		}
	}
	
	@Test
	void deviceFamiliarisingTest() {
		Assertions.assertEquals(server.getAllDiscoveredDeviceData().length, clients.length);
		Assertions.assertEquals(server.getAllKnownDeviceData().length, clients.length);
		
		IDeviceData[] dddServerActual = server.getAllDiscoveredDeviceData();
		IDeviceData[] kddServerActual = server.getAllKnownDeviceData();
		
		IDeviceData[] dddServerExpected = new IDeviceData[clients.length];
		IDeviceData[] kddServerExpected = new IDeviceData[clients.length];
		
		for (int i = 0; i < clients.length; i++) {
			dddServerExpected[i] = new DeviceData(clientNames[i], clientAddresses[i], false, false);
			kddServerExpected[i] = new DeviceData(clientNames[i], clientAddresses[i], true, true);
		}
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dddServerActual, dddServerExpected,
				(a, e) -> {
					return a.getDeviceName().equals(e.getDeviceName()) &&
							a.getDeviceAddress().equals(e.getDeviceAddress());
				}));
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddServerActual, kddServerExpected,
				(a, e) -> {
					return a.getDeviceName().equals(e.getDeviceName()) &&
							a.getDeviceAddress().equals(e.getDeviceAddress()) &&
							a.getIsAllowedToConnect() == e.getIsAllowedToConnect();
				}));
		
		// clients.length = other clients + server
		for (DummyClient client : clients) {
			
			IDeviceData[] dddClientActual = client.getAllDiscoveredDeviceData();
			IDeviceData[] kddClientActual = client.getAllKnownDeviceData();
			
			IDeviceData[] dddClientExpected = new IDeviceData[clients.length];
			IDeviceData[] kddClientExpected = new IDeviceData[] {new DeviceData(serverName, serverAddress, true, true)};
			
			for (int i = 0, a = 0; i < clients.length; i++) {
				if (!(client.getName().equals(clientNames[i]) &&
						client.getAddress().equals(clientAddresses[i]))) {
					dddClientExpected[a] = new DeviceData(clientNames[i], clientAddresses[i], false, false);
//					kddClientExpected[a] = new DeviceData(clientNames[i], clientAddresses[i], true, true);
					a++;
				}
			}
			
			for (DummyClient client1 : clients) {
				Assertions.assertNull(client.getConnection(client1.getAddress()));
			}
			
			dddClientExpected[clients.length - 1] = new DeviceData(serverName, serverAddress, true, true);
//			kddClientExpected[clients.length - 1] = new DeviceData(serverName, serverAddress, false, false);
			
			Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dddClientActual, dddClientExpected,
					(a, e) -> {
						return a.getDeviceName().equals(e.getDeviceName()) &&
								a.getDeviceAddress().equals(e.getDeviceAddress());
					}));
			
			Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(kddClientActual, kddClientExpected,
					(a, e) -> {
						return a.getDeviceName().equals(e.getDeviceName()) &&
								a.getDeviceAddress().equals(e.getDeviceAddress()) &&
								a.getIsAllowedToConnect() == e.getIsAllowedToConnect();
					}));
		}
	}
	
	@Test
	void menuExchangeTest() {
		interaction.setServerMenu(menu);
		
		for (DummyClient client : clients) {
			DishMenuData menuData = client.getMenuData();
			while (menuData.getAllItems().length == 0) {
				interaction.reSetServerMenu();
				GeneralTestUtilityClass.performWait(waitTime);
				menuData = client.getMenuData();
			}
			Assertions.assertTrue(server.getMenuData().equals(menuData));
		}
	}
	
	@Test
	void orderExchangeTest() {
		interaction.broadcastMenu(menu);
		
		for (int i = 0; i < clients.length; i++) {
			interaction.addPendingSendOrderToClient(clients[i], oids[i], sos[i]);
		}
		
		OrderData[] uoActual = server.getAllUnconfirmedOrders();
		OrderData[] uoExpected = new OrderData[clients.length];
		
		for (int i = 0; i < clients.length; i++) {
			uoExpected[i] = clients[i].deserialiseOrderData(sos[i]);
			Assertions.assertEquals(clients[i].getAllSentOrders().length, 1);
			Assertions.assertTrue(clients[i].getAllSentOrders()[0]
					.equals(clients[i].deserialiseOrderData(sos[i])));
		}
		
		Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(uoActual, uoExpected));
	}
	
	@Test
	void menuUpdateTest() {
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		
		for (DummyClient client : clients) {
			Assertions.assertTrue(client.menuDatasEqual(server));
		}
		
		menu.removeMenuItem(i1id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		
		for (DummyClient client : clients) {
			Assertions.assertTrue(client.menuDatasEqual(server));
		}
		
		menu.removeMenuItem(i2id);
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		
		for (DummyClient client : clients) {
			Assertions.assertTrue(client.menuDatasEqual(server));
		}
		
		menu.addMenuItem(server.createDishMenuItem(i1Name, i1PorSize, i1ProCost, i1Price, i1Name));
		menu.addMenuItem(server.createDishMenuItem(i2Name, i2PorSize, i2ProCost, i2Price, i2Name));
		interaction.broadcastMenu(menu);
		Assertions.assertTrue(server.menuEqual(menu));
		
		for (DummyClient client : clients) {
			Assertions.assertTrue(client.menuDatasEqual(server));
		}
	}
	
	@Test
	void multipleOrderExchangeTest() {
		interaction.broadcastMenu(menu);
		
		String[][] serialisedOrders = new String[oids.length][oids.length];
		Collection<String> soCols = new ArrayList<String>();
		ArrayList<ArrayList<String>> clientOrders = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < serialisedOrders.length; i++) {
			clientOrders.add(new ArrayList<String>());
		}
		
		for (int i = 0, a = 0; i < serialisedOrders.length; i++) {
			for (int j = 0; j < serialisedOrders[i].length; j++, a++) {
				String id = oids[i]+oids[j];
				serialisedOrders[i][j] = id+soBodys[j];
				interaction.addPendingSendOrderToClient(clients[j], id, serialisedOrders[i][j]);
				clientOrders.get(j).add(serialisedOrders[i][j]);
				Assertions.assertEquals(server.getAllUnconfirmedOrders().length, a+1);
				DummyClient dc = clients[j];
				Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(server.getAllUnconfirmedOrders(),
						soCols.stream().map(so -> dc.deserialiseOrderData(so))
						.toArray(OrderData[]::new)));
				
				Assertions.assertTrue(GeneralTestUtilityClass.arrayContentEquals(dc.getAllSentOrders(),
						clientOrders.get(j).stream().map(so -> dc.deserialiseOrderData(so))
						.toArray(OrderData[]::new)));
			}
			clientOrders.get(i).clear();
		}
		soCols.clear();
	}
}
