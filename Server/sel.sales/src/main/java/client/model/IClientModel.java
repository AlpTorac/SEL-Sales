package client.model;

import model.IModel;
import model.order.IOrderData;

public interface IClientModel extends IModel {
	void addCookingOrder(String serialisedOrderData);
	void makePendingPaymentOrder(String orderID);
	void makePendingSendOrder(String orderID);
	void makePastOrder(String orderID);
	IOrderData getCookingOrder(String orderID);
	IOrderData getPendingPaymentOrder(String orderID);
	IOrderData getPendingSendOrder(String orderID);
	IOrderData getPastOrder(String orderID);
	IOrderData[] getAllCookingOrders();
	IOrderData[] getAllPendingPaymentOrders();
	IOrderData[] getAllPendingSendOrders();
	IOrderData[] getAllPastOrders();
}
