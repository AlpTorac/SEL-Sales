package test.external.dummy;

import controller.IApplicationEvent;
import controller.IController;
import controller.handler.IApplicationEventHandler;
import model.connectivity.IClientData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public class DummyController implements IController {
	public DummyController() {
		
	}
	public void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler) {}
	public void handleApplicationEvent(IApplicationEvent event, Object[] args) {}
	public void addMenuItem(String serialisedItemData) {}
	public void editMenuItem(String serialisedNewItemData) {}
	public void addOrder(String serialisedOrder) {}
	public void removeOrder(String id) {}
	public void removeMenuItem(String id) {}
	public void confirmOrder(String serialisedConfirmedOrderData) {}
	public IOrderSerialiser getOrderSerialiser() {return null;}
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {return null;}
	public IDishMenuItemData getItem(String id) {return null;}
	public void addKnownClient(IClientData clientData) {}
}
