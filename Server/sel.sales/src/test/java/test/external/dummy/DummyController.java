package test.external.dummy;

import controller.IApplicationEvent;
import controller.IController;
import controller.StatusEvent;
import controller.handler.IApplicationEventHandler;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public class DummyController implements IController {
	public DummyController() {
		
	}
	public void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler) {}
	public void handleApplicationEvent(IApplicationEvent event, Object[] args) {
		if (event == StatusEvent.CLIENT_CONNECTED) {
			this.clientConnected((String) args[0]);
		}
		if (event == StatusEvent.CLIENT_DISCONNECTED) {
			this.clientDisconnected((String) args[0]);
		}
	}
	public void addMenuItem(String serialisedItemData) {}
	public void editMenuItem(String serialisedNewItemData) {}
	public void addOrder(String serialisedOrder) {}
	public void removeOrder(String id) {}
	public void removeMenuItem(String id) {}
	public void confirmOrder(String serialisedConfirmedOrderData) {}
	public IOrderSerialiser getOrderSerialiser() {return null;}
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {return null;}
	public IDishMenuItemData getItem(String id) {return null;}
	public void addKnownClient(String clientAddress) {}
	@Override
	public void addDiscoveredClient(String clientName, String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeKnownClient(String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void allowKnownClient(String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void blockKnownClient(String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clientConnected(String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void clientDisconnected(String clientAddress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void requestClientRediscovery() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void confirmAllOrders() {
		// TODO Auto-generated method stub
		
	}
}
