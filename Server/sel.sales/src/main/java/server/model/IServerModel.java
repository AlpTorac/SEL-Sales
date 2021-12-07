package server.model;

import model.IModel;
import model.dish.DishMenuItemData;
import model.order.OrderData;

public interface IServerModel extends IModel {
//	void addOrder(String serialisedOrderData);
//	default void addOrder(OrderData data) {
//		this.addUnconfirmedOrder(data);
//	}
	void addUnconfirmedOrder(OrderData data);
	void confirmOrder(OrderData data);
	void removeUnconfirmedOrder(String id);
	void removeConfirmedOrder(String id);
	
	void addMenuItem(DishMenuItemData data);
	void addMenuItem(String serialisedItemData);
	void editMenuItem(DishMenuItemData data);
	void removeMenuItem(String id);
	OrderData[] getAllUnconfirmedOrders();
	OrderData[] getAllConfirmedOrders();
	void removeAllUnconfirmedOrders();
	void removeAllConfirmedOrders();
//	@Override
//	default void removeAllOrders() {
//		this.removeAllUnconfirmedOrders();
//		this.removeAllConfirmedOrders();
//	}
	void confirmAllOrders();
	
	void setAutoConfirmOrders(boolean autoConfirm);
	boolean getAutoConfirmOrders();
	boolean writeDishMenu();
	void loadDishMenu(String fileAddress);
	boolean writeOrders();
}
