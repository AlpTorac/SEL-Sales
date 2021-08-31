package controller;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderIDFactory;

public interface IController {
	void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler);
	void handleBusinessEvent(BusinessEvent event, Object[] args);
	void addMenuItem(IDishMenuItemData args);
	void addOrder(String serialisedOrder);
	void removeMenuItem(IDishMenuItemID id);
	void confirmOrder(IOrderData orderData);
	IDishMenuItemData getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
	IOrderDataFactory getOrderDataCommunicationProtocoll();
	IOrderIDFactory getOrderItemDataCommunicationProtocoll();
}
