package test.external.dummy;

import java.io.Closeable;
import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

import controller.IController;
import external.connection.IConnection;
import external.device.IDevice;
import model.IModel;
import model.connectivity.IDeviceData;
import model.dish.DishMenu;
import model.dish.DishMenuData;
import model.dish.DishMenuItem;
import model.order.OrderData;
import model.settings.SettingsField;
import test.GeneralTestUtilityClass;
import view.IView;
import view.repository.IUILibraryHelper;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;

public abstract class DummyInteractionPartaker implements Closeable {
	private static final String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	private String name;
	private String address;
	private DummyDevice device;
	private DummyDeviceDiscoveryStrategy ddds;
	
	private String serviceName;
	private String serviceID;
	
	private IModel model;
	private IController controller;
	private IDummyExternal external;
	
	public DummyInteractionPartaker(String serviceName, String serviceID, String name, String address) {
		this.serviceName = serviceName;
		this.serviceID = serviceID;
		
		this.name = name;
		this.address = address;
	}
	
	public void start() {
		this.device = new DummyDevice(this.name, this.address);
		this.model = this.initModel();
		this.controller = this.initController();
		this.external = this.initExternal();
		
		ddds = new DummyDeviceDiscoveryStrategy();
		external.setDiscoveryStrategy(ddds);
	}
	
	protected abstract IModel initModel();
	protected abstract IController initController();
	protected abstract IDummyExternal initExternal();
	
	public String getServiceName() {
		return this.serviceName;
	}
	
	public String getServiceID() {
		return this.serviceID;
	}
	
	public IModel getModel() {
		return this.model;
	}
	
	public IController getController() {
		return this.controller;
	}
	
	public IDummyExternal getExternal() {
		return this.external;
	}
	
	protected String getTestFolderAddress() {
		return testFolderAddress;
	}

	public DummyDevice getDeviceObject() {
		return this.device;
	}

	public void discoverDevice(IDevice device) {
		ddds.addDiscoveredDevices(device);
		this.getModel().requestDeviceRediscovery(()->{});
	}

	public void discoverDevices(Collection<IDevice> devicesServer) {
		ddds.addDiscoveredDevices(devicesServer);
		this.getModel().requestDeviceRediscovery(()->{});
	}

	public void addKnownDevice(String deviceAddress) {
		this.getModel().addKnownDevice(deviceAddress);
	}

	public IConnection getConnection(String deviceAddress) {
		return this.getExternal().getConnection(deviceAddress);
	}

	@Override
	public void close() {
		this.getExternal().close();
		this.getModel().close();
	}
	
	public IDeviceData[] getAllDiscoveredDeviceData() {
		return this.getModel().getAllDiscoveredDeviceData();
	}
	
	public IDeviceData[] getAllKnownDeviceData() {
		return this.getModel().getAllKnownDeviceData();
	}
	
	public OrderData getOrder(String orderID) {
		return this.getModel().getOrder(orderID);
	}
	
	public boolean menuEqual(DishMenu menu) {
		return this.getModel().getMenuData().equals(this.getModel().getDishMenuHelper().dishMenuToData(menu));
	}
	
	public void setMenu(DishMenu menu) {
		this.setMenu(this.getModel().getDishMenuHelper().dishMenuToData(menu));
	}
	
	public void setMenu(DishMenuData menuData) {
		this.getModel().setDishMenu(menuData);
	}
	
	public DishMenuData getMenuData() {
		return this.getModel().getMenuData();
	}
	
	public void reSetMenu() {
		DishMenuData data = this.getMenuData();
		this.getModel().setDishMenu(data);
	}
	
	public OrderData deserialiseOrderData(String serialisedOrder) {
		return this.getModel().getOrderHelper().deserialiseOrderData(serialisedOrder);
	}
	
	public DishMenu createDishMenu() {
		return this.getModel().getDishMenuHelper().createDishMenu();
	}
	
	public DishMenuItem createDishMenuItem(String name, BigDecimal porSize, BigDecimal proCost, BigDecimal price, String id) {
		return this.getModel().getDishMenuHelper().createDishMenuItem(name, porSize, proCost, price, id);
	}
	
	public boolean menuDatasEqual(DummyInteractionPartaker dip) {
		return this.getModel().getMenuData().equals(dip.getModel().getMenuData());
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAddress() {
		return this.address;
	}

	public void blockDevice(String address2) {
		this.getModel().blockKnownDevice(address2);
	}
	
	public Collection<Integer> getTableNumbers() {
		return this.getModel().getTableNumbers();
	}
	
	public void setTableNumbers(String serialisedRanges) {
		this.getModel().addSetting(SettingsField.TABLE_NUMBERS, serialisedRanges);
	}
	
	public boolean tableNumbersEqual(DummyInteractionPartaker dip) {
		return this.getTableNumbers() != null && dip.getTableNumbers() != null &&
				this.getTableNumbers().size() == dip.getTableNumbers().size() &&
				GeneralTestUtilityClass.arrayContentEquals(this.getTableNumbers().toArray(Integer[]::new),
						dip.getTableNumbers().toArray(Integer[]::new),
						(tn1,tn2)->{return tn1.intValue() == tn2.intValue();});
	}
}