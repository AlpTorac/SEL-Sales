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
import model.datamapper.menu.DishMenuItemDAO;
import model.datamapper.order.OrderAttribute;
import model.datamapper.order.OrderDAO;
import model.dish.DishMenuItemFinder;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;
import model.dish.DishMenu;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.dish.DishMenuItemFactory;
import model.filemanager.FileManager;
import model.filemanager.IFileManager;
import model.order.Order;
import model.order.OrderCollector;
import model.order.OrderData;
import model.order.OrderFactory;
import model.order.OrderStatus;
import model.settings.HasSettingsField;
import model.settings.ISettings;
import model.settings.ISettingsParser;
import model.settings.ISettingsSerialiser;
import model.settings.Settings;
import model.settings.SettingsField;
import model.settings.StandardSettingsParser;
import model.settings.StandardSettingsSerialiser;
import model.settings.TableNumberContainer;

public abstract class Model implements IModel {

	private Collection<Updatable> updatables;
	private Collection<HasSettingsField> part;
	
	private DishMenu dishMenu;
	private DishMenuItemFinder finder;
	
	private DishMenuItemFactory menuItemFac;
	private OrderFactory orderFac;
	
	private IConnectivityManager connManager;
	
	private IFileManager fileManager;
	
	private ISettings settings;
	private ISettingsParser settingsParser;
	private ISettingsSerialiser settingsSerialiser;
	
	private FileDeviceDataParser deviceDataParser;
	private FileDeviceDataSerialiser deviceDataSerialiser;

	private NumberDisplaySettings nds;
	private DateSettings ds;
	
	private OrderCollector orderCollector;
	
	private OrderDAO orderDAO;
	private DishMenuItemDAO menuDAO;
	
	private TableNumberContainer tnc;
	
	private String resourceFolderAddress = "src/main/resources";
	
	private EntityIDFactory idFac;
	
	protected Model() {
		this.idFac = this.initIDFactory();
		
		this.updatables = new ArrayList<Updatable>();
		this.part = new ArrayList<HasSettingsField>();
		
		this.nds = new NumberDisplaySettings();
		this.ds = new DateSettings();
		this.orderFac = new OrderFactory();
		this.menuItemFac = new DishMenuItemFactory();
		
		this.orderCollector = this.initOrderCollector();
		
		this.deviceDataParser = new FileDeviceDataParser();
		this.deviceDataSerialiser = new FileDeviceDataSerialiser();
		
		this.setDishMenu(this.initDishMenu());
		
		this.connManager = new ConnectivityManager();
		
		this.settings = new Settings();
		this.settingsParser = new StandardSettingsParser();
		this.settingsSerialiser = new StandardSettingsSerialiser();
		
		this.fileManager = new FileManager(this, this.resourceFolderAddress);
		this.tnc = new TableNumberContainer(this);
		
		this.orderDAO = new OrderDAO(this.resourceFolderAddress);
		this.orderDAO.setFinder(this.finder);
		this.menuDAO = new DishMenuItemDAO(this.resourceFolderAddress);
		
		this.part.add(this.tnc);
		this.part.add(this.fileManager);
	}
	
	protected OrderDAO getOrderDAO() {
		return this.orderDAO;
	}
	
	protected DishMenuItemDAO getDishMenuItemDAO() {
		return this.menuDAO;
	}
	
	@Override
	public String serialiseOrder(OrderData data) {
		return this.getOrderDAO().serialiseValueObject(data);
	}
	
	@Override
	public abstract void addOrder(OrderData data);
//		this.getOrderCollector().addElement(data);
//	}
	
	@Override
	public void addOrder(String serialisedOrderData) {
		this.addOrder(this.getOrderDAO().parseValueObject(serialisedOrderData));
	}
	
	@Override
	public OrderFactory getOrderFactory() {
		return this.orderFac;
	}
	
	@Override
	public DishMenuItemFactory getMenuItemFactory() {
		return this.menuItemFac;
	}
	
	protected OrderCollector initOrderCollector() {
		return new OrderCollector();
	}
	
	protected DishMenu initDishMenu() {
		return this.createDishMenu();
	}
	
	protected DishMenu createDishMenu() {
		return new DishMenu();
	}
	
	protected EntityIDFactory initIDFactory() {
		return new MinimalIDFactory();
	}
	
	protected EntityIDFactory getIDFactory() {
		return this.idFac;
	}
	
	protected EntityID createMinimalID(String id) {
		return this.getIDFactory().createID(id);
	}
	
	public OrderData getOrder(EntityID id) {
//		OrderData order = this.orderUnconfirmedCollector.getOrder(id);
//		
//		if (order == null) {
//			order = this.orderConfirmedCollector.getOrder(id);
//		}
		return this.getOrderCollector().getElementAsValueObject(id);
	}
	
	@Override
	public OrderData getOrder(String id) {
		return this.getOrder(this.createMinimalID(id));
	}
	
	protected OrderCollector getOrderCollector() {
		return this.orderCollector;
	}
	
//	protected abstract OrderCollector getWrittenOrderCollector();

	protected IFileManager getFileManager() {
		return this.fileManager;
	}
	
	protected DishMenu getDishMenu() {
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
		this.notifySettingsChange(p -> true,
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
	
	public DishMenuItemData getMenuItem(EntityID id) {
		return this.getDishMenu().getElementAsValueObject(id);
	}
	
	@Override
	public DishMenuItemData getMenuItem(String id) {
		return this.getMenuItem(this.createMinimalID(id));
	}

	@Override
	public DishMenuData getMenuData() {
		return this.getDishMenu().toData();
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

	protected void setDishMenu(DishMenu dishMenu) {
		this.dishMenu = dishMenu;
		this.finder = new DishMenuItemFinder(this.getDishMenu());
		if (this.getOrderDAO() != null) {
			this.getOrderDAO().setFinder(this.finder);
		}
		this.menuChanged();
	}
	
	protected void setDishMenu(Iterable<DishMenuItemData> dishMenu) {
		DishMenu menu = new DishMenu();
		for (DishMenuItemData data : dishMenu) {
			menu.addElement(data);
		}
		this.setDishMenu(menu);
	}

	@Override
	public void setDishMenuFromFile(String menu) {
		this.setDishMenu(this.getDishMenuItemDAO().parseValueObjects(menu));
	}
	
	@Override
	public void setDishMenuFromExternal(String menu) {
		this.setDishMenu(this.getDishMenuItemDAO().parseValueObjects(menu));
	}
	
	@Override
	public void setDishMenu(DishMenuData menu) {
		this.setDishMenu(menu.toDishMenu());
		this.menuChanged();
	}

	@Override
	public void close() {
		this.getFileManager().close();
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
	public void loadSaved() {
		this.getFileManager().loadSaved();
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
	public DishMenuItemFinder getActiveDishMenuItemFinder() {
		return this.finder;
	}

	@Override
	public void addSetting(SettingsField sf, String serialisedValue) {
		this.settings.addSetting(sf, serialisedValue);
		this.settingsChanged();
	}

	public void writeAllOrders() {
		OrderData[] datas = this.getOrderCollector().getAllElementsAsValueObjects().toArray(OrderData[]::new);
		this.getFileManager().writeOrderDatas(this.getOrderDAO().serialiseValueObjects(datas));
		for (OrderData d : datas) {
			this.getOrderCollector().setAttributeValue(OrderAttribute.IS_WRITTEN, d.getID(), true);
		}
	}
	
	public boolean writeOrder(EntityID orderID) {
		OrderData[] datas = this.getOrderCollector().getAllElementsAsValueObjects().toArray(OrderData[]::new);
		this.getFileManager().writeOrderDatas(this.getOrderDAO().serialiseValueObjects(datas));
		for (OrderData d : datas) {
			this.getOrderCollector().setAttributeValue(OrderAttribute.IS_WRITTEN, d.getID(), true);
		}
		return true;
	}
	
	@Override
	public boolean writeOrder(String orderID) {
		return this.writeOrder(this.createMinimalID(orderID));
	}
	
	public boolean isOrderWritten(EntityID orderID) {
		Object o = this.getOrderCollector().getAttributeValue(OrderAttribute.IS_WRITTEN, orderID);
		if (o == null) {
			return false;
		}
		return (boolean) o;
	}
	
	@Override
	public boolean isOrderWritten(String orderID) {
		return this.isOrderWritten(this.createMinimalID(orderID));
	}
	
	@Override
	public void setWrittenOrders(String readFile) {
		if (readFile == null) {
			return;
		}
		for (OrderData od : this.getOrderDAO().parseValueObjects(readFile)) {
			this.addWrittenOrder(od);
		}
	}
	
	protected void addWrittenOrder(OrderData data) {
		this.getOrderCollector().addElement(data);
		this.ordersChanged();
//		this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, data.getID(), OrderStatus.PAST);
//		this.getOrderCollector().setAttributeValue(OrderAttribute.IS_WRITTEN, data.getID(), true);
	}
	
	@Override
	public NumberDisplaySettings getNumberDisplaySettings() {
		return this.nds;
	}
	
	@Override
	public DateSettings getDateSettings() {
		return this.ds;
	}
	
	@Override
	public OrderData[] getAllWrittenOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.IS_WRITTEN, true)).toArray(OrderData[]::new);
	}
	@Override
	public Collection<Integer> getTableNumbers() {
		return this.tnc.getAllTableNumbers();
	}
	@Override
	public boolean tableExists(int number) {
		return this.tnc.tableExists(number);
	}
	
	@Override
	public void removeAllOrders() {
		for (Order d : this.getOrderCollector().getAllElements()) {
			this.removeOrder(d.getID());
		}
	}
	
	public void removeOrder(EntityID id) {
		this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, id, OrderStatus.CANCELLED);
		this.getOrderCollector().removeElement(id);
		this.writeAllOrders();
		this.ordersChanged();
	}
	
	@Override
	public void removeOrder(String id) {
		this.removeOrder(this.createMinimalID(id));
	}
	
	@Override
	public boolean isOrderValid(String orderID) {
		return this.getOrder(orderID) != null &&
				!this.getOrderCollector().getAttributeValue(OrderAttribute.STATUS, this.createMinimalID(orderID))
				.equals(OrderStatus.CANCELLED);
	}
	
	@Override
	public String serialiseMenuData() {
		return this.getDishMenuItemDAO().serialiseValueObjects(this.getMenuData().getAllElements().toArray(DishMenuItemData[]::new));
	}
	
	@Override
	public String serialiseMenuItem(DishMenuItemData data) {
		return this.getDishMenuItemDAO().serialiseValueObject(data);
	}
	
	@Override
	public void clearAllOrders() {
		this.getOrderCollector().clearAllElements();
	}
}