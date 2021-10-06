package model;

import java.util.ArrayList;
import java.util.Collection;

import model.connectivity.ConnectivityManager;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import model.dish.DishMenu;
import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemFinder;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.dish.serialise.IntraAppDishMenuItemSerialiser;
import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemDeserialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuSerialiser;
import model.dish.serialise.StandardDishMenuDeserialiser;
import model.filewriter.DishMenuFileWriter;
import model.filewriter.OrderFileWriter;
import model.filewriter.StandardDishMenuFileWriter;
import model.filewriter.StandardOrderFileWriter;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.OrderCollector;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IOrderSerialiser;
import model.order.serialise.IntraAppOrderSerialiser;
import model.order.serialise.StandardOrderDeserialiser;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	
	private IDishMenu dishMenu;
	private IDishMenuDataFactory dishMenuDataFac;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderSerialiser orderSerialiser;
	private IOrderDeserialiser orderDeserialiser;
	
	private IDishMenuItemDeserialiser dishMenuItemDeserialiser;
	private IDishMenuItemFinder finder;
	private IDishMenuItemSerialiser menuItemSerialiser;
	
	/**
	 * Only for the external clients (outside the server part of the app)
	 */
	private IDishMenuSerialiser externalDishMenuSerialiser;
	
	private IConnectivityManager connManager;
	
	private String orderFolderAddress;
	private OrderFileWriter orderWriter;
	
	private String dishMenuFolderAddress;
	private DishMenuFileWriter dishMenuWriter;

	public Model() {
		this.updatables = new ArrayList<Updatable>();
		
		this.dishMenu = new DishMenu();
		this.dishMenuDataFac = new DishMenuDataFactory();
		
		this.orderSerialiser = new IntraAppOrderSerialiser();
		this.menuItemSerialiser = new IntraAppDishMenuItemSerialiser();
		this.dishMenuItemDeserialiser = new StandardDishMenuDeserialiser();
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderUnconfirmedCollector = new OrderCollector();
		this.orderConfirmedCollector = new OrderCollector();
		this.orderDeserialiser = new StandardOrderDeserialiser(this.finder);
		
		this.orderFolderAddress = "src/main/resources/orders";
		this.orderWriter = new StandardOrderFileWriter(this.orderFolderAddress);
		
		this.dishMenuFolderAddress = "src/main/resources/dishMenuItems";
		this.dishMenuWriter = new StandardDishMenuFileWriter(this.dishMenuFolderAddress);
		
		this.externalDishMenuSerialiser = new ExternalDishMenuSerialiser();
		
		this.connManager = new ConnectivityManager();
	}
	
	private void menuChanged() {
		this.updatables.stream().filter(u -> u instanceof MenuUpdatable).forEach(u -> ((MenuUpdatable) u).refreshMenu());
	}
	
	private void unconfirmedOrdersChanged() {
		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshUnconfirmedOrders());
	}
	
	private void confirmedOrdersChanged() {
		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmedOrders());
	}
	
	private void discoveredClientsChanged() {
		this.updatables.stream().filter(u -> u instanceof DiscoveredClientUpdatable).forEach(u -> ((DiscoveredClientUpdatable) u).refreshDiscoveredClients());
	}
	
	private void knownClientsChanged() {
		this.updatables.stream().filter(u -> u instanceof KnownClientUpdatable).forEach(u -> ((KnownClientUpdatable) u).refreshKnownClients());
	}
	
	private void externalStatusChanged() {
		this.updatables.stream().filter(u -> u instanceof ExternalUpdatable).forEach(u -> ((ExternalUpdatable) u).rediscoverClients());
	}
	
	public void addMenuItem(String serialisedItemData) {
		IDishMenuItemData data = this.dishMenuItemDeserialiser.deserialise(serialisedItemData);
		if (this.dishMenu.addMenuItem(data)) {
			this.menuChanged();
		}
	}

	public void removeMenuItem(String id) {
		if (this.dishMenu.removeMenuItem(id)) {
			this.menuChanged();
		}
	}

	public IDishMenuItemData getMenuItem(String id) {
		return this.dishMenu.getItem(id);
	}

	@Override
	public IDishMenuData getMenuData() {
		return this.dishMenuDataFac.dishMenuToData(this.dishMenu);
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}

	@Override
	public void addUnconfirmedOrder(String orderData) {
		IOrderData order = this.orderDeserialiser.deserialise(orderData);
		this.orderUnconfirmedCollector.addOrder(order);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public IOrderData getOrder(String id) {
		IOrderData order = this.orderUnconfirmedCollector.getOrder(id);
		
		if (order == null) {
			order = this.orderConfirmedCollector.getOrder(id);
		}
		
		return order;
	}

	@Override
	public IOrderData[] getAllUnconfirmedOrders() {
		return this.orderUnconfirmedCollector.getAllOrders();
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.orderUnconfirmedCollector.clearOrders();
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void editMenuItem(String serialisedNewItemData) {
		IDishMenuItemData data = dishMenuItemDeserialiser.deserialise(serialisedNewItemData);
		this.dishMenu.editMenuItem(data);
		this.menuChanged();
	}

	@Override
	public void confirmOrder(String serialisedConfirmedOrderData) {
		IOrderData orderData = this.orderDeserialiser.deserialise(serialisedConfirmedOrderData);
		this.orderUnconfirmedCollector.removeOrder(orderData.getID());
		this.orderConfirmedCollector.addOrder(orderData);
		this.unconfirmedOrdersChanged();
		this.confirmedOrdersChanged();
	}

	@Override
	public IOrderData[] getAllConfirmedOrders() {
		return this.orderConfirmedCollector.getAllOrders();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
		this.orderUnconfirmedCollector.removeOrder(id);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void removeConfirmedOrder(String id) {
		this.orderConfirmedCollector.removeOrder(id);
		this.confirmedOrdersChanged();
	}

	@Override
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {
		return this.menuItemSerialiser;
	}

	@Override
	public IOrderSerialiser getOrderSerialiser() {
		return this.orderSerialiser;
	}

	@Override
	public void removeAllConfirmedOrders() {
		this.orderConfirmedCollector.clearOrders();
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeOrders() {
		boolean b = true;
		for (IOrderData d : this.getAllConfirmedOrders()) {
			b = b && this.orderWriter.writeOrderData(d);
		}
		return b;
	}

	@Override
	public boolean writeDishMenu() {
		return this.dishMenuWriter.writeDishMenuData(this.dishMenuDataFac.dishMenuToData(this.dishMenu));
	}

	@Override
	public IDishMenuSerialiser getExternalDishMenuSerialiser() {
		return this.externalDishMenuSerialiser;
	}

	@Override
	public void addDiscoveredClient(String clientName, String clientAddress) {
		this.connManager.addDiscoveredClient(clientName, clientAddress);
		this.discoveredClientsChanged();
	}

	@Override
	public void addKnownClient(String clientAddress) {
		this.connManager.addKnownClient(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public void removeKnownClient(String clientAddress) {
		this.connManager.removeKnownClient(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public void allowKnownClient(String clientAddress) {
		this.connManager.allowKnownClient(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public void blockKnownClient(String clientAddress) {
		this.connManager.blockKnownClient(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public IClientData[] getAllKnownClientData() {
		return this.connManager.getAllKnownClientData();
	}
	
	@Override
	public IClientData[] getAllDiscoveredClientData() {
		return this.connManager.getAllDiscoveredClientData();
	}

	@Override
	public void clientConnected(String clientAddress) {
		this.connManager.clientConnected(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public void clientDisconnected(String clientAddress) {
		this.connManager.clientDisconnected(clientAddress);
		this.knownClientsChanged();
	}

	@Override
	public boolean isClientRediscoveryRequested() {
		return this.connManager.isClientRediscoveryRequested();
	}

	@Override
	public void requestClientRediscovery() {
		this.connManager.requestClientRediscovery();
		this.externalStatusChanged();
	}

	@Override
	public void confirmAllOrders() {
		IOrderData[] unconfirmedOrders = this.orderUnconfirmedCollector.getAllOrders();
		for (IOrderData uco : unconfirmedOrders) {
			this.orderConfirmedCollector.addOrder(uco);
		}
		this.orderUnconfirmedCollector.clearOrders();
		this.confirmedOrdersChanged();
		this.unconfirmedOrdersChanged();
	}
}
