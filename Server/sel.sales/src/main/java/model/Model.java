package model;

import java.util.ArrayList;
import java.util.Collection;

import model.connectivity.ConnectivityManager;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import model.dish.DishMenu;
import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemDataFactory;
import model.dish.DishMenuItemFinder;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.dish.serialise.IntraAppDishMenuItemSerialiser;
import model.dish.serialise.DishMenuFormat;
import model.dish.serialise.DishMenuParser;
import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemDeserialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuParser;
import model.dish.serialise.IDishMenuSerialiser;
import model.dish.serialise.StandardDishMenuDeserialiser;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.filewriter.FileDishMenuFormat;
import model.filewriter.FileOrderSerialiser;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;
import model.order.OrderCollector;
import model.order.OrderDataFactory;
import model.order.OrderItemDataFactory;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IOrderSerialiser;
import model.order.serialise.IntraAppOrderSerialiser;
import model.order.serialise.StandardOrderDeserialiser;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.SettingsParser;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;

public class Model implements IModel {
	
	private volatile boolean autoConfirmOrders = false;
	
	private Collection<Updatable> updatables;
	private Collection<HasSettingsField> part;
	
	private IDishMenu dishMenu;
	private IDishMenuParser dishMenuParser;
	private IDishMenuDataFactory dishMenuDataFac;
	private IDishMenuItemDataFactory dishMenuItemDataFac;
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IntraAppOrderSerialiser appOrderSerialiser;
	private FileOrderSerialiser fileOrderSerialiser;
	private IOrderDeserialiser orderDeserialiser;
	
	private IDishMenuItemDeserialiser dishMenuItemDeserialiser;
	private IDishMenuItemFinder finder;
	private IDishMenuItemSerialiser menuItemSerialiser;
	private IDishMenuSerialiser fileMenuSerialiser;
	/**
	 * Only for the external clients (outside the server part of the app)
	 */
	private IDishMenuSerialiser externalDishMenuSerialiser;
	
	private IConnectivityManager connManager;
	
	private IFileManager fileManager;

	private ISettings settings;
	private ISettingsParser settingsParser;
	private ISettingsSerialiser settingsSerialiser;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.part = new ArrayList<HasSettingsField>();
		
		this.dishMenu = new DishMenu();
		this.dishMenuItemDataFac = new DishMenuItemDataFactory();
		this.dishMenuDataFac = new DishMenuDataFactory(this.dishMenuItemDataFac);
		this.dishMenuParser = new DishMenuParser(new FileDishMenuFormat(), this.dishMenuDataFac);
		this.orderItemDataFac = new OrderItemDataFactory();
		this.orderDataFac = new OrderDataFactory(orderItemDataFac);
		
		this.appOrderSerialiser = new IntraAppOrderSerialiser();
		this.fileOrderSerialiser = new FileOrderSerialiser();
		this.menuItemSerialiser = new IntraAppDishMenuItemSerialiser();
		this.fileMenuSerialiser = new FileDishMenuSerialiser();
		this.dishMenuItemDeserialiser = new StandardDishMenuDeserialiser();
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderUnconfirmedCollector = new OrderCollector();
		this.orderConfirmedCollector = new OrderCollector();
		this.orderDeserialiser = new StandardOrderDeserialiser(this.finder);
		
		this.externalDishMenuSerialiser = new ExternalDishMenuSerialiser();
		
		this.connManager = new ConnectivityManager();
		
		this.settings = new Settings();
		this.settingsParser = new StandardSettingsParser();
		this.settingsSerialiser = new StandardSettingsSerialiser();
//		this.settings.addSetting(SettingsField.ORDER_FOLDER, "src/main/resources/orders");
//		this.settings.addSetting(SettingsField.DISH_MENU_FOLDER, "src/main/resources/dishMenu");
		
		this.fileManager = new FileManager(this, "src/main/resources");
		this.part.add(fileManager);
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
	
	private void orderConfirmModeChanged() {
		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmMode());
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
	
	private void settingsChanged() {
		this.part.stream().forEach(p -> p.refreshValue());
		this.updatables.stream().filter(u -> u instanceof SettingsUpdatable).forEach(u -> ((SettingsUpdatable) u).refreshSettings());
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
		IDishMenuItem item = this.dishMenu.getItem(id);
		if (item != null) {
			return this.dishMenuItemDataFac.menuItemToData(item);
		}
		return null;
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
		if (this.autoConfirmOrders) {
			this.confirmOrder(orderData);
		} else {
			this.unconfirmedOrdersChanged();
		}
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
		return this.appOrderSerialiser;
	}

	@Override
	public void removeAllConfirmedOrders() {
		this.orderConfirmedCollector.clearOrders();
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeOrders() {
		return this.fileManager.writeOrderDatas(this.fileOrderSerialiser.serialiseOrderDatas(this.getAllConfirmedOrders()));
	}

	@Override
	public boolean writeDishMenu() {
		return this.fileManager.writeDishMenuData(this.fileMenuSerialiser.serialise(this.dishMenuDataFac.dishMenuToData(this.dishMenu)));
	}
	
	@Override
	public boolean writeSettings() {
		return this.fileManager.writeSettings(this.settingsSerialiser.serialise(this.getSettings()));
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

	@Override
	public void setOrderFolderAddress(String address) {
		this.settings.changeSettingValue(SettingsField.ORDER_FOLDER, address);
		this.settingsChanged();
	}

	@Override
	public void setDishMenuFolderAddress(String address) {
		this.settings.changeSettingValue(SettingsField.DISH_MENU_FOLDER, address);
		this.settingsChanged();
	}

	@Override
	public void removeOrder(String id) {
		this.removeConfirmedOrder(id);
		this.removeUnconfirmedOrder(id);
	}

	@Override
	public void setAutoConfirmOrders(boolean autoConfirm) {
		this.autoConfirmOrders = autoConfirm;
		if (this.autoConfirmOrders) {
			this.confirmAllOrders();
		}
		this.orderConfirmModeChanged();
	}

	@Override
	public boolean getAutoConfirmOrders() {
		return this.autoConfirmOrders;
	}

	@Override
	public ISettings getSettings() {
		return this.settings;
	}

	@Override
	public void setSettings(String settings) {
		this.settings = this.settingsParser.parseSettings(settings);
		this.settingsChanged();
	}

	@Override
	public void setDishMenu(String menu) {
		this.dishMenu = new DishMenu();
		IDishMenuData menuData = this.dishMenuParser.parseDishMenuData(menu);
		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
			this.dishMenu.addMenuItem(data);
		}
		this.menuChanged();
	}

	@Override
	public void loadSaved() {
		this.fileManager.loadSaved();
	}
	@Override
	public void close() {
		this.fileManager.close();
	}
}
