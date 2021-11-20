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
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItem;
import model.order.IOrderData;
import test.GeneralTestUtilityClass;

public abstract class DummyInteractionPartaker implements Closeable {

	private static final long waitTime = 100;
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
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void discoverDevices(Collection<IDevice> devicesServer) {
		ddds.addDiscoveredDevices(devicesServer);
		this.getModel().requestDeviceRediscovery(()->{});
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void addKnownDevice(String deviceAddress) {
		this.getModel().addKnownDevice(deviceAddress);
		GeneralTestUtilityClass.performWait(waitTime);
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
	
	public IOrderData getOrder(String orderID) {
		return this.getModel().getOrder(orderID);
	}
	
	public boolean menuEqual(IDishMenu menu) {
		return this.getModel().getMenuData().equals(this.getModel().getDishMenuHelper().dishMenuToData(menu));
	}
	
	public void setMenu(IDishMenu menu) {
		this.setMenu(this.getModel().getDishMenuHelper().dishMenuToData(menu));
	}
	
	public void setMenu(IDishMenuData menuData) {
		this.getModel().setDishMenu(menuData);
	}
	
	public IDishMenuData getMenuData() {
		return this.getModel().getMenuData();
	}
	
	public void reSetMenu() {
		IDishMenuData data = this.getMenuData();
		this.getModel().setDishMenu(data);
	}
	
	public IOrderData deserialiseOrderData(String serialisedOrder) {
		return this.getModel().getOrderHelper().deserialiseOrderData(serialisedOrder);
	}
	
	public IDishMenu createDishMenu() {
		return this.getModel().getDishMenuHelper().createDishMenu();
	}
	
	public IDishMenuItem createDishMenuItem(String name, BigDecimal porSize, BigDecimal proCost, BigDecimal price, String id) {
		return this.getModel().getDishMenuHelper().createDishMenuItem(name, porSize, proCost, price, id);
	}
	
	public boolean menuDatasEqual(DummyInteractionPartaker dip) {
		return this.getModel().getMenuData().equals(dip.getModel().getMenuData());
	}
}