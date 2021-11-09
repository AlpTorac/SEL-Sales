package server.model;

import model.IModel;
import model.order.IOrderData;

public interface IServerModel extends IModel {
	default void addOrder(String serialisedOrderData) {
		this.addUnconfirmedOrder(serialisedOrderData);
	}
	void addUnconfirmedOrder(String serialisedOrderData);
	void confirmOrder(String serialisedConfirmedOrderData);
	void removeUnconfirmedOrder(String id);
	void removeConfirmedOrder(String id);
	void addMenuItem(String serialisedItemData);
	void editMenuItem(String serialisedNewItemData);
	void removeMenuItem(String id);
	IOrderData[] getAllUnconfirmedOrders();
	IOrderData[] getAllConfirmedOrders();
	void removeAllUnconfirmedOrders();
	void removeAllConfirmedOrders();
	@Override
	default void removeAllOrders() {
		this.removeAllUnconfirmedOrders();
		this.removeAllConfirmedOrders();
	}
	void confirmAllOrders();
	
	void setAutoConfirmOrders(boolean autoConfirm);
	boolean getAutoConfirmOrders();
	boolean writeDishMenu();
}
