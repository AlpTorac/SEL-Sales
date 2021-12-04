package model;

import java.io.Closeable;
import java.util.Collection;

import model.connectivity.IDeviceData;
import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.dish.DishMenuItemFactory;
import model.dish.DishMenuItemFinder;
import model.order.OrderData;
import model.order.OrderFactory;
import model.settings.ISettings;
import model.settings.SettingsField;

public interface IModel extends Closeable {
	void addOrder(OrderData data);
	void addOrder(String serialisedOrderData);
	void subscribe(Updatable updatable);
	DishMenuItemData getMenuItem(String id);
	DishMenuData getMenuData();
	OrderData getOrder(String id);
	OrderData[] getAllWrittenOrders();
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
	/**
	 * Removes all orders without changing their attributes
	 */
	void clearAllOrders();
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
	void setDishMenu(DishMenuData menu);
	void setKnownDevices(String serialisedDeviceData);
	void loadSaved();
	void loadKnownDevices(String fileAddress);
	void loadOrders(String fileAddress);
	boolean writeSettings();
	void close();
	void setWrittenOrders(String readFile);
	DishMenuItemFinder getActiveDishMenuItemFinder();
	boolean isOrderWritten(String orderID);
	DateSettings getDateSettings();
	Collection<Integer> getTableNumbers();
	boolean tableExists(int tableNumber);
	void setOrderTableNumber(String orderID, int tableNumber);
	void setOrderNote(String orderID, String note);
	/**
	 * Checks if the order exists and has not been cancelled
	 */
	boolean isOrderValid(String orderID);
	OrderFactory getOrderFactory();
	DishMenuItemFactory getMenuItemFactory();
	String serialiseOrder(OrderData data);
	String serialiseMenuItem(DishMenuItemData data);
	String serialiseMenuData();
}