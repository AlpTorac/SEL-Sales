package client.model;

import model.IModel;
import model.order.OrderData;

public interface IClientModel extends IModel {
	void addCookingOrder(OrderData data);
	void makePendingPaymentOrder(String orderID);
	void makePendingSendOrder(OrderData data);
	void makeSentOrder(String orderID);
	OrderData getCookingOrder(String orderID);
	OrderData getPendingPaymentOrder(String orderID);
	OrderData getPendingSendOrder(String orderID);
	OrderData getSentOrder(String orderID);
	OrderData[] getAllCookingOrders();
	OrderData[] getAllPendingPaymentOrders();
	OrderData[] getAllPendingSendOrders();
	OrderData[] getAllSentOrders();
	void editOrder(String orderID);
	OrderData getEditTarget();
	void orderSentByID(String orderID);
	void orderSentBySerialisedVersion(String serialisedOrderData);
}
