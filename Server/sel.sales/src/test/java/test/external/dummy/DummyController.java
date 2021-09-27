package test.external.dummy;

import controller.BusinessEvent;
import controller.IController;
import controller.handler.IBusinessEventHandler;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public class DummyController implements IController {
	public DummyController() {
		
	}
	public void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler) {}
	public void handleBusinessEvent(BusinessEvent event, Object[] args) {}
	public void addMenuItem(String serialisedItemData) {}
	public void editMenuItem(String serialisedNewItemData) {}
	public void addOrder(String serialisedOrder) {}
	public void removeOrder(String id) {}
	public void removeMenuItem(String id) {}
	public void confirmOrder(String serialisedConfirmedOrderData) {}
	public IOrderSerialiser getOrderSerialiser() {return null;}
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {return null;}
	public IDishMenuItemData getItem(String id) {return null;}

}
