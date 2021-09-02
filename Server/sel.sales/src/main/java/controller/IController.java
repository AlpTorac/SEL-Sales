package controller;

import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public interface IController {
	void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler);
	void handleBusinessEvent(BusinessEvent event, Object[] args);
	void addMenuItem(String serialisedItemData);
	void editMenuItem(String serialisedNewItemData);
	void addOrder(String serialisedOrder);
	void removeOrder(String id);
	void removeMenuItem(String id);
	void confirmOrder(String serialisedConfirmedOrderData);
	IOrderSerialiser getOrderSerialiser();
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IDishMenuItemData getItem(String id);
}
