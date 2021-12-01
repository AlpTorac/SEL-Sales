package test.external.dummy;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import external.device.IDevice;
import model.dish.IDishMenu;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;

public class DummyInteraction implements Closeable {
	private static long waitTime = 100;
	
	private DummyServer server;
	private Collection<DummyClient> clients;
	
	private Collection<ConnectionPair> connPairs;
	
	public DummyInteraction(DummyServer server, DummyClient... clients) {
		this.clients = new CopyOnWriteArrayList<DummyClient>(clients);
		this.connPairs = new CopyOnWriteArrayList<ConnectionPair>();
		this.server = server;
		
		this.initialStart();
		
		this.initialDiscovery();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	protected void initialStart() {
		this.server.start();
		this.clients.forEach(c -> c.start());
	}
	
	public void setServerTableNumbers(String serialisedRanges) {
		server.setTableNumbers(serialisedRanges);
	}
	
	public void setServerMenu(IDishMenu menu) {
		server.setMenu(menu);
	}
	
	public void reSetServerMenu() {
		server.reSetMenu();
	}
	
	/**
	 * Server sends its menu data to all the connected clients
	 */
	public void broadcastMenu(IDishMenu menu) {
		this.setServerMenu(menu);
		GeneralTestUtilityClass.performWait(waitTime);
		for (DummyClient client : this.clients) {
			while (client.getMenuData().getAllDishMenuItems().length == 0 || !client.menuDatasEqual(server)) {
				this.reSetServerMenu();
				GeneralTestUtilityClass.performWait(waitTime);
			}
		}
	}
	
	public void broadcastTableNumbers(String serialisedRanges) {
		this.setServerTableNumbers(serialisedRanges);
		GeneralTestUtilityClass.performWait(waitTime);
		for (DummyClient client : this.clients) {
			while (!client.tableNumbersEqual(server)) {
				this.setServerTableNumbers(serialisedRanges);
				GeneralTestUtilityClass.performWait(waitTime);
			}
		}
	}
	
	public void addPendingSendOrderToClient(DummyClient client, String orderID, String serialisedOrder) {
		client.addCookingOrder(serialisedOrder);
		client.makePendingPaymentOrder(orderID);
		client.makePendingSendOrder(orderID, serialisedOrder);
		while (server.getOrder(orderID) == null ||
				!GeneralTestUtilityClass.arrayContains(client.getAllSentOrders(),
						client.deserialiseOrderData(serialisedOrder))) {
			GeneralTestUtilityClass.performWait(waitTime);
			client.refreshOrders();
		}
	}
	
	protected void initialDiscovery() {
		Collection<DummyInteractionPartaker> dips = new ArrayList<DummyInteractionPartaker>();
		dips.add(server);
		clients.forEach(c -> dips.add(c));
		
		DummyInteractionPartaker[] dipArr = dips.toArray(DummyInteractionPartaker[]::new);
		
		for (DummyInteractionPartaker dip : dipArr) {
			Collection<IDevice> devices = new ArrayList<IDevice>();
			dips.stream().forEach(d ->
					{
						if (!d.equals(dip)) {
							devices.add(d.getDeviceObject());
						}
					}
					);
			dip.discoverDevices(devices);
		}
	}
	
	public void allowConnection(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		dip1.addKnownDevice(dip2.getAddress());
		GeneralTestUtilityClass.performWait(waitTime);
		dip2.addKnownDevice(dip1.getAddress());
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void blockConnection(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		dip1.blockDevice(dip2.getAddress());
		GeneralTestUtilityClass.performWait(waitTime);
		dip2.blockDevice(dip1.getAddress());
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void disconnect(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		try {
			dip1.getConnection(dip2.getAddress()).close();
			dip2.getConnection(dip1.getAddress()).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected ConnectionPair bindConnections(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		DummyConnection conn1 = (DummyConnection) dip1.getConnection(dip2.getAddress());
		DummyConnection conn2 = (DummyConnection) dip2.getConnection(dip1.getAddress());
		
		while (conn1 == null || conn2 == null) {
			System.out.println("waiting for connections");
			GeneralTestUtilityClass.performWait(100);
			conn1 = (DummyConnection) dip1.getConnection(dip2.getAddress());
			System.out.println("Conn1: " + conn1);
			conn2 = (DummyConnection) dip2.getConnection(dip1.getAddress());
			System.out.println("Conn2: " + conn2);
		}
		InteractionUtilityClass.bindConnectionStreams(conn1, conn2);
		return this.createConnectionPair(conn1, conn2);
	}
	
	protected ConnectionPair createConnectionPair(DummyConnection conn1, DummyConnection conn2) {
		return new ConnectionPair(conn1, conn2);
	}
	
	protected void addConnectionPair(ConnectionPair connPair) {
		this.connPairs.add(connPair);
	}
	
	protected void removeConnectionPair(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		this.connPairs
		.removeIf(cp -> cp.connectionPairEquals(
				(DummyConnection) dip1.getConnection(dip2.getAddress()),
				(DummyConnection) dip2.getConnection(dip1.getAddress())
				));
	}
	
	public void resetConnection(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		this.disconnectByBlocking(dip1, dip2);
		this.connectPartakers(dip1, dip2);
	}
	
	public void connectPartakers(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		this.allowConnection(dip1, dip2);
		this.addConnectionPair(this.bindConnections(dip1, dip2));
	}
	
	public void disconnectByBlocking(DummyInteractionPartaker dip1, DummyInteractionPartaker dip2) {
		this.blockConnection(dip1, dip2);
		this.removeConnectionPair(dip1, dip2);
	}
	
	public DummyServer getServer() {
		return this.server;
	}
	
	public DummyClient getClient(String clientName, String clientAddress) {
		Optional<DummyClient> o = this.clients.stream()
				.filter(c -> c.getAddress().equals(clientAddress))
				.filter(c -> c.getName().equals(clientName))
				.findFirst();
		
		return o.isPresent() ? o.get() : null;
	}
	
	protected void forEachClient(Consumer<DummyClient> consumer) {
		this.clients.forEach(c -> consumer.accept(c));
	}
	
	public void close() {
		if (this.server != null) {
			this.server.close();
		}
		this.clients.forEach(dc -> {
			if (dc != null) {
				dc.close();
			}
		});
	}
	
	protected class ConnectionPair {
		private DummyConnection conn1;
		private DummyConnection conn2;
		
		protected ConnectionPair(DummyConnection conn1, DummyConnection conn2) {
			this.conn1 = conn1;
			this.conn2 = conn2;
		}
		
		public boolean connectionPairEquals(DummyConnection conn1, DummyConnection conn2) {
			if (conn1 == null || conn2 == null) {
				return false;
			}
			return (this.conn1.equals(conn1) && this.conn2.equals(conn2)) ||
					(this.conn1.equals(conn2) && this.conn2.equals(conn1));
		}
	}
}
