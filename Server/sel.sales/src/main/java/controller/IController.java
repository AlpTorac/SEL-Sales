package controller;

import controller.handler.IApplicationEventHandler;
import model.IModel;

public interface IController {
	void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler);
	void handleApplicationEvent(IApplicationEvent event, Object[] args);
	IModel getModel();
//	void addMenuItem(String serialisedItemData);
//	void editMenuItem(String serialisedNewItemData);
//	void addOrder(String serialisedOrder);
//	void removeOrder(String id);
//	void removeMenuItem(String id);
//	void confirmOrder(String serialisedConfirmedOrderData);
//	
//	void addDiscoveredClient(String clientName, String clientAddress);
//	void addKnownClient(String clientAddress);
//	void removeKnownClient(String clientAddress);
//	void allowKnownClient(String clientAddress);
//	void blockKnownClient(String clientAddress);
//	void requestClientRediscovery();
//	
//	void clientConnected(String clientAddress);
//	void clientDisconnected(String clientAddress);
//	
//	IOrderSerialiser getOrderSerialiser();
//	IDishMenuItemSerialiser getDishMenuItemSerialiser();
//	IDishMenuItemData getItem(String id);
//	void confirmAllOrders();
//	
//	boolean writeOrders();
//	boolean writeDishMenu();
}
