package model;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;
import model.order.IOrderID;

public interface IModel {
	void addOrder(String serialisedOrderData);
	void addMenuItem(IDishMenuItemData item);
	void removeMenuItem(IDishMenuItemID item);
	void subscribe(Updatable updatable);
	IDishMenuItem getMenuItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
	IDishMenuItemData[] getMenuData();
	IOrderData getOrder(IOrderID id);
	IOrderData[] getAllOrders();
}
