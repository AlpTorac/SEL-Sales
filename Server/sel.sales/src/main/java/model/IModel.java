package model;

import java.io.Closeable;
import java.util.Collection;

import model.connectivity.IDeviceData;
import model.dish.IDishMenuData;
import model.dish.IDishMenuHelper;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderHelper;
import model.settings.ISettings;
import model.settings.SettingsField;

public interface IModel extends Closeable {
	void addOrder(String serialisedOrderData);
	void subscribe(Updatable updatable);
	IDishMenuItemData getMenuItem(String id);
	IDishMenuData getMenuData();
	IOrderData getOrder(String id);
	IOrderData[] getAllWrittenOrders();
	IDishMenuHelper getDishMenuHelper();
	IOrderHelper getOrderHelper();
	void addDiscoveredDevice(String deviceName, String deviceAddress);
	void addKnownDevice(String deviceAddress);
	void removeKnownDevice(String deviceAddress);
	void allowKnownDevice(String deviceAddress);
	void blockKnownDevice(String deviceAddress);
	boolean isDeviceRediscoveryRequested();
	void requestDeviceRediscovery(Runnable afterDiscoveryAction);
	void deviceConnected(String deviceAddress);
	void deviceDisconnected(String deviceAddress);
	IDeviceData[] getAllKnownDeviceData();
	IDeviceData[] getAllDiscoveredDeviceData();
	void removeAllOrders();
	void removeOrder(String id);
	/**
	 * @return True, if the order is written (now or in the past)
	 */
	boolean writeOrder(String orderID);
	ISettings getSettings();
	void addSetting(SettingsField sf, String serialisedValue);
	void setSettings(String settings);
	void setSettings(ISettings settings);
	void setSettings(String[][] settings);
	void setDishMenuFromFile(String menu);
	void setDishMenuFromExternal(String menu);
	void setDishMenu(IDishMenuData menu);
	void setKnownDevices(String serialisedDeviceData);
	void loadSaved();
	void loadKnownDevices(String fileAddress);
	void loadOrders(String fileAddress);
	boolean writeSettings();
	void close();
	void setWrittenOrders(String readFile);
	IDishMenuItemFinder getActiveDishMenuItemFinder();
	boolean isOrderWritten(String orderID);
	IDateSettings getDateSettings();
	void setOrderTableNumbersFromFile(String readFile);
	void setOrderStatuses(String readFile);
	Collection<Integer> getTableNumbers();
	boolean tableExists(int tableNumber);
	void setOrderTableNumber(String orderID, int tableNumber);
	Integer getOrderTableNumber(String orderID);
}