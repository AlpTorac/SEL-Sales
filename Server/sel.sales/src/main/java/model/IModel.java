package model;

import model.connectivity.IClientData;
import model.connectivity.IClientDataFactory;
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
	void addKnownClient(IClientData clientData);
	void subscribe(Updatable updatable);
	IDishMenuItemData getMenuItem(String id);
	IDishMenuData getMenuData();
	IOrderData getOrder(String id);
	IOrderData[] getAllUnconfirmedOrders();
	IOrderData[] getAllConfirmedOrders();
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IDishMenuSerialiser getExternalDishMenuSerialiser();
	IOrderSerialiser getOrderSerialiser();
	IClientDataFactory getClientDataFactory();
	void removeAllUnconfirmedOrders();
	void removeAllConfirmedOrders();
	default void removeAllOrders() {
		this.removeAllUnconfirmedOrders();
		this.removeAllConfirmedOrders();
	}
	boolean writeOrders();
	boolean writeDishMenu();
}
