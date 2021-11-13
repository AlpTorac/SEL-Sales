package client.model;

import model.IModel;
import model.order.IOrderData;

public interface IClientModel extends IModel {
	void addCookingOrder(String serialisedOrderData);
	void makePendingPaymentOrder(String orderID);
	void makePendingSendOrder(String formerID, String serialisedOrderData);
	void makeSentOrder(String orderID);
	IOrderData getCookingOrder(String orderID);
	IOrderData getPendingPaymentOrder(String orderID);
	IOrderData getPendingSendOrder(String orderID);
	IOrderData getSentOrder(String orderID);
	IOrderData[] getAllCookingOrders();
	IOrderData[] getAllPendingPaymentOrders();
	IOrderData[] getAllPendingSendOrders();
	IOrderData[] getAllSentOrders();
	void editOrder(String orderID);
	IOrderData getEditTarget();
	void orderSent(String orderID);
}
