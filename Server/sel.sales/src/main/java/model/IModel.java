package model;

import java.io.Closeable;

import model.connectivity.IClientData;
import model.dish.IDishMenuData;
import model.dish.IDishMenuHelper;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderHelper;
import model.settings.ISettings;
import model.settings.ISettingsFactory;
import model.settings.SettingsField;

public interface IModel extends Closeable {
	void addUnconfirmedOrder(String serialisedOrderData);
	void confirmOrder(String serialisedConfirmedOrderData);
	void removeUnconfirmedOrder(String id);
	void removeConfirmedOrder(String id);
	void addMenuItem(String serialisedItemData);
	void editMenuItem(String serialisedNewItemData);
	void removeMenuItem(String id);
	void subscribe(Updatable updatable);
	IDishMenuItemData getMenuItem(String id);
	IDishMenuData getMenuData();
	IOrderData getOrder(String id);
	IOrderData[] getAllUnconfirmedOrders();
	IOrderData[] getAllConfirmedOrders();
	IOrderData[] getAllWrittenOrders();
	IDishMenuHelper getDishMenuHelper();
	IOrderHelper getOrderHelper();
	
	void addDiscoveredClient(String clientName, String clientAddress);
	void addKnownClient(String clientAddress);
	void removeKnownClient(String clientAddress);
	void allowKnownClient(String clientAddress);
	void blockKnownClient(String clientAddress);
	boolean isClientRediscoveryRequested();
	void requestClientRediscovery(Runnable afterDiscoveryAction);
	
	void clientConnected(String clientAddress);
	void clientDisconnected(String clientAddress);
	
	IClientData[] getAllKnownClientData();
	IClientData[] getAllDiscoveredClientData();
	
	void removeAllUnconfirmedOrders();
	void removeAllConfirmedOrders();
	default void removeAllOrders() {
		this.removeAllUnconfirmedOrders();
		this.removeAllConfirmedOrders();
	}
	void removeOrder(String id);
	
	boolean writeOrders();
	boolean writeDishMenu();
	void confirmAllOrders();
	
	void setAutoConfirmOrders(boolean autoConfirm);
	boolean getAutoConfirmOrders();
	
	ISettings getSettings();
	void addSetting(SettingsField sf, String serialisedValue);
	void setSettings(String settings);
	void setSettings(ISettings settings);
	void setSettings(String[][] settings);
	void setDishMenu(String menu);
	void setKnownClients(String serialisedClientData);
	
	void loadSaved();
	void loadDishMenu(String fileAddress);
	void loadKnownClients(String fileAddress);
	void loadOrders(String fileAddress);
	boolean writeSettings();
	
	void close();
	void setWrittenOrders(String readFile);
	
	IDishMenuItemFinder getActiveDishMenuItemFinder();
}
