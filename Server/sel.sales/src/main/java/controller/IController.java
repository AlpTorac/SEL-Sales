package controller;

import controller.handler.IApplicationEventHandler;
import model.connectivity.IClientData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public interface IController {
	void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler);
	void handleApplicationEvent(IApplicationEvent event, Object[] args);
	void addMenuItem(String serialisedItemData);
	void editMenuItem(String serialisedNewItemData);
	void addOrder(String serialisedOrder);
	void removeOrder(String id);
	void removeMenuItem(String id);
	void confirmOrder(String serialisedConfirmedOrderData);
	void addKnownClient(IClientData clientData);
	IOrderSerialiser getOrderSerialiser();
	IDishMenuItemSerialiser getDishMenuItemSerialiser();
	IDishMenuItemData getItem(String id);
}
