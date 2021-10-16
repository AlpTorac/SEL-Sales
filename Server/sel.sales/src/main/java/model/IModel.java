package model;

import model.connectivity.IClientData;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuSerialiser;
import model.order.IOrderData;
import model.order.serialise.IOrderSerialiser;

public interface IModel {
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
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IDishMenuSerialiser getExternalDishMenuSerialiser();
	IOrderSerialiser getOrderSerialiser();
	
	void addDiscoveredClient(String clientName, String clientAddress);
	void addKnownClient(String clientAddress);
	void removeKnownClient(String clientAddress);
	void allowKnownClient(String clientAddress);
	void blockKnownClient(String clientAddress);
	boolean isClientRediscoveryRequested();
	void requestClientRediscovery();
	
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
	
	void setOrderFolderAddress(String address);
	void setDishMenuFolderAddress(String address);
}
