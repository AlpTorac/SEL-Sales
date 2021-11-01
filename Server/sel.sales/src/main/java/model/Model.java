package model;

import java.util.ArrayList;
import java.util.Collection;

import model.connectivity.ConnectivityManager;
import model.connectivity.FileClientDataParser;
import model.connectivity.FileClientDataSerialiser;
import model.connectivity.IClientData;
import model.connectivity.IConnectivityManager;
import model.dish.DishMenu;
import model.dish.DishMenuHelper;
import model.dish.DishMenuItemFinder;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuHelper;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.IOrderHelper;
import model.order.OrderHelper;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;

public class Model implements IModel {
	
	private volatile boolean autoConfirmOrders = false;
	
	private Collection<Updatable> updatables;
	private Collection<HasSettingsField> part;
	
	private IOrderHelper orderHelper;
	private IDishMenuHelper menuHelper;
	
	private IDishMenu dishMenu;
	private IDishMenuItemFinder finder;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderCollector writtenOrderCollector;
	
	private IConnectivityManager connManager;
	
	private IFileManager fileManager;

	private ISettings settings;
	private ISettingsParser settingsParser;
	private ISettingsSerialiser settingsSerialiser;
	
	private FileClientDataParser clientDataParser;
	private FileClientDataSerialiser clientDataSerialiser;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.part = new ArrayList<HasSettingsField>();
		
		this.orderHelper = new OrderHelper();
		this.menuHelper = new DishMenuHelper();
		
		this.clientDataParser = new FileClientDataParser();
		this.clientDataSerialiser = new FileClientDataSerialiser();
		
		this.setDishMenu(this.menuHelper.createDishMenu());
		
		this.orderUnconfirmedCollector = this.orderHelper.createOrderCollector();
		this.orderConfirmedCollector = this.orderHelper.createOrderCollector();
		this.writtenOrderCollector = this.orderHelper.createOrderCollector();
		
		this.connManager = new ConnectivityManager();
		
		this.settings = new Settings();
		this.settingsParser = new StandardSettingsParser();
		this.settingsSerialiser = new StandardSettingsSerialiser();
		
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
		this.fileManager.writeClientDatas(this.clientDataSerialiser.serialiseClientDatas(this.connManager.getAllKnownClientData()));
	}
	
	private void externalStatusChanged(Runnable afterDiscoveryAction) {
		this.updatables.stream().filter(u -> u instanceof ExternalUpdatable).forEach(u -> ((ExternalUpdatable) u).rediscoverClients(afterDiscoveryAction));
	}
	
	private void settingsChanged() {
		this.part.stream().forEach(p -> p.refreshValue());
		this.updatables.stream().filter(u -> u instanceof SettingsUpdatable).forEach(u -> ((SettingsUpdatable) u).refreshSettings());
	}
	
	private void ordersChanged() {
		this.unconfirmedOrdersChanged();
		this.confirmedOrdersChanged();
	}
	
	public void addMenuItem(String serialisedItemData) {
//		IDishMenuItemData data = this.dishMenuItemDeserialiser.deserialise(serialisedItemData);
		if (this.dishMenu.addMenuItem(this.menuHelper.createDishMenuItem(serialisedItemData))) {
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
			return this.menuHelper.dishMenuItemToData(item);
//			return this.dishMenuItemDataFac.menuItemToData(item);
		}
		return null;
	}

	@Override
	public IDishMenuData getMenuData() {
		return this.menuHelper.dishMenuToData(this.dishMenu);
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}

	@Override
	public void addUnconfirmedOrder(String orderData) {
		IOrderData order = this.orderHelper.deserialiseOrderData(orderData);
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
		IDishMenuItemData data = this.menuHelper.deserialiseDishMenuItem(serialisedNewItemData);
		this.dishMenu.editMenuItem(data);
		this.menuChanged();
	}

	protected void confirmOrder(IOrderData orderData) {
		this.orderUnconfirmedCollector.removeOrder(orderData.getID().toString());
		this.orderConfirmedCollector.addOrder(orderData);
		if (!this.isOrderWritten(orderData.getID().toString())) {
			this.fileManager.writeOrderData(this.orderHelper.serialiseForFile(orderData));
			this.writtenOrderCollector.addOrder(orderData);
		}
		this.ordersChanged();
	}
	
	@Override
	public void confirmOrder(String serialisedConfirmedOrderData) {
		IOrderData orderData = this.orderHelper.deserialiseOrderData(serialisedConfirmedOrderData);
		this.confirmOrder(orderData);
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
	public void removeAllConfirmedOrders() {
		this.orderConfirmedCollector.clearOrders();
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeOrders() {
		IOrderData[] orders = this.getAllConfirmedOrders();
		Collection<IOrderData> ordersToBeWritten = new ArrayList<IOrderData>();
		for (IOrderData od : orders) {
			if (!this.isOrderWritten(od.getID().toString())) {
				ordersToBeWritten.add(od);
			}
		}
		IOrderData[] array = ordersToBeWritten.toArray(IOrderData[]::new);
		boolean allWritten = this.fileManager.writeOrderData(this.orderHelper.serialiseForFile(array));
		if (allWritten) {
			for (IOrderData od : array) {
				this.writtenOrderCollector.addOrder(od);
			}
		}
		return allWritten;
	}

	@Override
	public boolean writeDishMenu() {
		return this.fileManager.writeDishMenuData(this.menuHelper.serialiseMenuForFile(this.menuHelper.dishMenuToData(this.dishMenu)));
	}
	
	@Override
	public boolean writeSettings() {
		return this.fileManager.writeSettings(this.settingsSerialiser.serialise(this.getSettings()));
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
	public void requestClientRediscovery(Runnable afterDiscoveryAction) {
		this.connManager.requestClientRediscovery();
		this.externalStatusChanged(afterDiscoveryAction);
	}

	@Override
	public void confirmAllOrders() {
		IOrderData[] unconfirmedOrders = this.orderUnconfirmedCollector.getAllOrders();
		for (IOrderData uco : unconfirmedOrders) {
			this.confirmOrder(uco);
		}
//		this.orderUnconfirmedCollector.clearOrders();
		this.ordersChanged();
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
		this.ordersChanged();
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

	private void setDishMenu(IDishMenu dishMenu) {
		this.dishMenu = dishMenu;
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderHelper.setFinder(this.finder);
	}
	
	@Override
	public void setDishMenu(String menu) {
		IDishMenu dishMenu = new DishMenu();
		IDishMenuData menuData = this.menuHelper.parseMenuData(menu);
		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
			dishMenu.addMenuItem(this.menuHelper.dishMenuItemDataToItem(data));
		}
		this.setDishMenu(dishMenu);
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

	@Override
	public IDishMenuHelper getDishMenuHelper() {
		return this.menuHelper;
	}

	@Override
	public IOrderHelper getOrderHelper() {
		return this.orderHelper;
	}

	@Override
	public void loadDishMenu(String fileAddress) {
		this.fileManager.loadDishMenu(fileAddress);
	}

	@Override
	public void setKnownClients(String serialisedClientData) {
		IClientData[] clientDatas = this.clientDataParser.parseClientDatas(serialisedClientData);
		if (clientDatas != null) {
			this.requestClientRediscovery(()->{
				for (IClientData d : clientDatas) {
					this.addDiscoveredClient(d.getClientName(), d.getClientAddress());
					this.addKnownClient(d.getClientAddress());
					if (d.getIsAllowedToConnect()) {
						this.allowKnownClient(d.getClientAddress());
					} else {
						this.blockKnownClient(d.getClientAddress());
					}
				}
			});
		}
	}

	@Override
	public void loadKnownClients(String fileAddress) {
		this.fileManager.loadKnownClients(fileAddress);
	}

	private boolean isOrderWritten(String orderID) {
		return this.writtenOrderCollector.getOrder(orderID) != null;
	}
	
	@Override
	public void setWrittenOrders(String readFile) {
		if (readFile == null) {
			return;
		}
		IOrderData[] orderData = this.orderHelper.deserialiseOrderDatas(readFile);
		for (IOrderData od : orderData) {
			this.writtenOrderCollector.addOrder(od);
		}
	}

	@Override
	public void loadOrders(String fileAddress) {
		this.fileManager.loadOrders(fileAddress);
	}

	@Override
	public IOrderData[] getAllWrittenOrders() {
		return this.writtenOrderCollector.getAllOrders();
	}

	@Override
	public IDishMenuItemFinder getActiveDishMenuItemFinder() {
		return this.finder;
	}
}
