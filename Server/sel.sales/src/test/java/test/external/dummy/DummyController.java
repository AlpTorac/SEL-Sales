package test.external.dummy;

import controller.BusinessEvent;
import controller.IApplicationEvent;
import controller.IController;
import controller.StatusEvent;
import controller.handler.IApplicationEventHandler;
import model.IModel;
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
		if (event == BusinessEvent.ADD_ORDER) {
			this.addOrder(null);
		}
	}
	public void clientConnected(String arg) {
		
	}
	public void clientDisconnected(String arg) {
		
	}
	@Override
	public IModel getModel() {
		return null;
	}
	public void addOrder(String serialisedOrder) {
		
	}
}
