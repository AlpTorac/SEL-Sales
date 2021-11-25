package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import model.connectivity.ConnectivityManager;
import model.connectivity.FileDeviceDataParser;
import model.connectivity.FileDeviceDataSerialiser;
import model.connectivity.IConnectivityManager;
import model.connectivity.IDeviceData;
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
import model.order.OrderStatus;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;

public abstract class Model implements IModel {

	private Collection<Updatable> updatables;
	private Collection<HasSettingsField> part;
	
	private IOrderHelper orderHelper;
	private IDishMenuHelper menuHelper;
	
	private IDishMenu dishMenu;
	private IDishMenuItemFinder finder;
	
	private IConnectivityManager connManager;
	
	private IFileManager fileManager;
	
	private ISettings settings;
	private ISettingsParser settingsParser;
	private ISettingsSerialiser settingsSerialiser;
	
	private FileDeviceDataParser deviceDataParser;
	private FileDeviceDataSerialiser deviceDataSerialiser;

	private IDateSettings ds;
	
	private IOrderCollector orderCollector;
	
	protected Model() {
		this.updatables = new ArrayList<Updatable>();
		this.part = new ArrayList<HasSettingsField>();
		
		this.ds = new DateSettings();
		
		this.orderHelper = new OrderHelper();
		this.menuHelper = new DishMenuHelper();
		
		this.orderCollector = this.orderHelper.createOrderCollector();
		
		this.deviceDataParser = new FileDeviceDataParser();
		this.deviceDataSerialiser = new FileDeviceDataSerialiser();
		
		this.setDishMenu(this.menuHelper.createDishMenu());
		
		this.connManager = new ConnectivityManager();
		
		this.settings = new Settings();
		this.settingsParser = new StandardSettingsParser();
		this.settingsSerialiser = new StandardSettingsSerialiser();
		
		this.fileManager = new FileManager(this, "src/main/resources");
		this.part.add(this.fileManager);
	}
	
	protected IOrderCollector getOrderCollector() {
		return this.orderCollector;
	}
	
//	protected abstract IOrderCollector getWrittenOrderCollector();

	protected IFileManager getFileManager() {
		return this.fileManager;
	}
	
	protected IDishMenu getDishMenu() {
		return this.dishMenu;
	}
	
	protected void notifyUpdatableChange(Predicate<? super Updatable> filter, Consumer<? super Updatable> action) {
		this.updatables.stream().filter(filter).forEach(action);
	}
	
	protected void notifySettingsChange(Predicate<? super HasSettingsField> filter, Consumer<? super HasSettingsField> action) {
		this.part.stream().filter(filter).forEach(action);
	}
	
	protected void menuChanged() {
		this.notifyUpdatableChange(u -> u instanceof MenuUpdatable,
				u -> ((MenuUpdatable) u).refreshMenu());
//		System.out.println(this + " Menu changed");
//		this.updatables.stream().filter(u -> u instanceof MenuUpdatable).forEach(u -> ((MenuUpdatable) u).refreshMenu());
	}

	protected void discoveredDevicesChanged() {
		this.notifyUpdatableChange(u -> u instanceof DiscoveredDeviceUpdatable,
				u -> ((DiscoveredDeviceUpdatable) u).refreshDiscoveredDevices());
//		this.updatables.stream().filter(u -> u instanceof DiscoveredDeviceUpdatable).forEach(u -> ((DiscoveredDeviceUpdatable) u).refreshDiscoveredDevices());
	}

	protected void knownDevicesChanged() {
		this.notifyUpdatableChange(u -> u instanceof KnownDeviceUpdatable,
				u -> ((KnownDeviceUpdatable) u).refreshKnownDevices());
//		this.updatables.stream().filter(u -> u instanceof KnownDeviceUpdatable).forEach(u -> ((KnownDeviceUpdatable) u).refreshKnownDevices());
		this.getFileManager().writeDeviceDatas(this.deviceDataSerialiser.serialiseDeviceDatas(this.connManager.getAllKnownDeviceData()));
	}

	protected void externalStatusChanged(Runnable afterDiscoveryAction) {
		this.notifyUpdatableChange(u -> u instanceof ExternalUpdatable,
				u -> ((ExternalUpdatable) u).rediscoverDevices(afterDiscoveryAction));
//		this.updatables.stream().filter(u -> u instanceof ExternalUpdatable).forEach(u -> ((ExternalUpdatable) u).rediscoverDevices(afterDiscoveryAction));
	}

	protected void settingsChanged() {
		this.notifySettingsChange(u -> true,
				p -> p.refreshValue());
		this.notifyUpdatableChange(u -> u instanceof SettingsUpdatable,
				u -> ((SettingsUpdatable) u).refreshSettings());
//		this.part.stream().forEach(p -> p.refreshValue());
//		this.updatables.stream().filter(u -> u instanceof SettingsUpdatable).forEach(u -> ((SettingsUpdatable) u).refreshSettings());
	}
	
	protected void ordersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderUpdatable,
				u -> ((OrderUpdatable) u).refreshOrders());
	}

	public IDishMenuItemData getMenuItem(String id) {
			IDishMenuItem item = this.getDishMenu().getItem(id);
			if (item != null) {
				return this.getDishMenuHelper().dishMenuItemToData(item);
	//			return this.dishMenuItemDataFac.menuItemToData(item);
			}
			return null;
		}

	@Override
	public IDishMenuData getMenuData() {
		return this.getDishMenuHelper().dishMenuToData(this.getDishMenu());
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}

	@Override
	public boolean writeSettings() {
		return this.getFileManager().writeSettings(this.settingsSerialiser.serialise(this.getSettings()));
	}

	@Override
	public void addDiscoveredDevice(String deviceName, String deviceAddress) {
		this.connManager.addDiscoveredDevice(deviceName, deviceAddress);
		this.discoveredDevicesChanged();
	}

	@Override
	public void addKnownDevice(String deviceAddress) {
		this.connManager.addKnownDevice(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public void removeKnownDevice(String deviceAddress) {
		this.connManager.removeKnownDevice(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public void allowKnownDevice(String deviceAddress) {
		this.connManager.allowKnownDevice(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public void blockKnownDevice(String deviceAddress) {
		this.connManager.blockKnownDevice(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public IDeviceData[] getAllKnownDeviceData() {
		return this.connManager.getAllKnownDeviceData();
	}

	@Override
	public IDeviceData[] getAllDiscoveredDeviceData() {
		return this.connManager.getAllDiscoveredDeviceData();
	}

	@Override
	public void deviceConnected(String deviceAddress) {
		this.connManager.deviceConnected(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public void deviceDisconnected(String deviceAddress) {
		this.connManager.deviceDisconnected(deviceAddress);
		this.knownDevicesChanged();
	}

	@Override
	public boolean isDeviceRediscoveryRequested() {
		return this.connManager.isDeviceRediscoveryRequested();
	}

	@Override
	public void requestDeviceRediscovery(Runnable afterDiscoveryAction) {
		this.connManager.requestDeviceRediscovery();
		this.externalStatusChanged(afterDiscoveryAction);
	}

	@Override
	public ISettings getSettings() {
		return this.settings;
	}

	@Override
	public void setSettings(String settings) {
		this.setSettings(this.settingsParser.parseSettings(settings));
	}

	@Override
	public void setSettings(ISettings settings) {
		this.settings = settings;
		this.settingsChanged();
	}

	@Override
	public void setSettings(String[][] settings) {
		this.settings.addAllSettings(settings);
		this.settingsChanged();
	}

	protected void setDishMenu(IDishMenu dishMenu) {
		this.dishMenu = dishMenu;
		this.finder = new DishMenuItemFinder(this.getDishMenu());
		this.orderHelper.setFinder(this.finder);
//		if (dishMenu != null) {
//			System.out.println(this+" New menu: " + this.getDishMenuHelper().serialiseForExternal(
//					this.getDishMenuHelper().dishMenuToData(this.dishMenu)
//					));
//		}
	}

	@Override
	public void setDishMenuFromFile(String menu) {
//		IDishMenu dishMenu = new DishMenu();
//		IDishMenuData menuData = this.menuHelper.parseMenuData(menu);
//		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
//			dishMenu.addMenuItem(this.menuHelper.dishMenuItemDataToItem(data));
//		}
//		this.setDishMenu(dishMenu);
//		this.menuChanged();
//		System.out.println(this+" serialised menu received: " + menu);
//		try {
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		this.setDishMenu(this.menuHelper.parseFileMenuData(menu));
	}
	
	@Override
	public void setDishMenuFromExternal(String menu) {
//		IDishMenu dishMenu = new DishMenu();
//		IDishMenuData menuData = this.menuHelper.parseMenuData(menu);
//		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
//			dishMenu.addMenuItem(this.menuHelper.dishMenuItemDataToItem(data));
//		}
//		this.setDishMenu(dishMenu);
//		this.menuChanged();
//		System.out.println(this+" serialised menu received: " + menu);
//		try {
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		this.setDishMenu(this.menuHelper.parseExternalMenuData(menu));
	}
	
	@Override
	public void setDishMenu(IDishMenuData menu) {
		IDishMenu dishMenu = this.getDishMenuHelper().createDishMenu();
		for (IDishMenuItemData data : menu.getAllDishMenuItems()) {
			dishMenu.addMenuItem(this.menuHelper.dishMenuItemDataToItem(data));
		}
//		System.out.println(this+" Menu with items: " + this.getDishMenuHelper().serialiseForExternal(
//				this.getDishMenuHelper().dishMenuToData(dishMenu)
//				));
		this.setDishMenu(dishMenu);
		this.menuChanged();
	}

	@Override
	public void close() {
		this.getFileManager().close();
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
	public void setKnownDevices(String serialisedDeviceData) {
		IDeviceData[] deviceDatas = this.deviceDataParser.parseDeviceDatas(serialisedDeviceData);
		if (deviceDatas != null) {
			this.requestDeviceRediscovery(()->{
				for (IDeviceData d : deviceDatas) {
					this.addDiscoveredDevice(d.getDeviceName(), d.getDeviceAddress());
					this.addKnownDevice(d.getDeviceAddress());
					if (d.getIsAllowedToConnect()) {
						this.allowKnownDevice(d.getDeviceAddress());
					} else {
						this.blockKnownDevice(d.getDeviceAddress());
					}
				}
			});
		}
	}

	@Override
	public void loadKnownDevices(String fileAddress) {
		this.getFileManager().loadKnownDevices(fileAddress);
	}

	@Override
	public void loadOrders(String fileAddress) {
		this.getFileManager().loadOrders(fileAddress);
	}

	@Override
	public IDishMenuItemFinder getActiveDishMenuItemFinder() {
		return this.finder;
	}

	@Override
	public void addSetting(SettingsField sf, String serialisedValue) {
		this.settings.addSetting(sf, serialisedValue);
		this.settingsChanged();
	}

	@Override
	public boolean writeOrder(String orderID) {
		if (!this.isOrderWritten(orderID)) {
			boolean isWritten = this.getFileManager().writeOrderData(this.getOrderHelper().serialiseForFile(this.getOrder(orderID)));
			this.getOrderCollector().editWritten(orderID, isWritten);
			return isWritten;
		}
		return true;
	}
	
	@Override
	public boolean isOrderWritten(String orderID) {
//		for (IOrderData data : this.getAllWrittenOrders()) {
//			if (data.getID().serialisedIDequals(orderID)) {
//				return true;
//			}
//		}
		return this.getOrderCollector().isWritten(orderID);
	}
	
	@Override
	public void setWrittenOrders(String readFile) {
		if (readFile == null) {
			return;
		}
		IOrderData[] orderData = this.getOrderHelper().deserialiseOrderDatas(readFile);
		for (IOrderData od : orderData) {
			this.addWrittenOrder(od);
		}
	}
	
	protected void addWrittenOrder(IOrderData data) {
//		this.writtenOrderCollector.addOrder(data);
		this.getOrderCollector().addOrder(data, OrderStatus.PAST);
		this.getOrderCollector().editWritten(data.getID().toString(), true);
	}
	
	@Override
	public IDateSettings getDateSettings() {
		return this.ds;
	}
	
	@Override
	public IOrderData[] getAllWrittenOrders() {
//		return this.writtenOrderCollector.getAllOrders();
		return this.getOrderCollector().getAllWrittenOrders();
	}
}