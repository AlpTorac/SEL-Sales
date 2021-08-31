package model;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderID;
import model.order.IOrderIDFactory;

public interface IModel {
	void addUnconfirmedOrder(String serialisedOrderData);
	void addConfirmedOrder(IOrderData orderData);
	void addMenuItem(IDishMenuItemData item);
	void editMenuItem(IDishMenuItemData newItem);
	void removeMenuItem(IDishMenuItemID item);
	void subscribe(Updatable updatable);
	IDishMenuItem getMenuItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
	IOrderDataFactory getOrderDataCommunicationProtocoll();
	IOrderIDFactory getOrderItemDataCommunicationProtocoll();
	IDishMenuItemData[] getMenuData();
	IOrderData getOrder(IOrderID id);
	IOrderData[] getAllUnconfirmedOrders();
	IOrderData[] getAllConfirmedOrders();
	void removeAllUnconfirmedOrders();
}
