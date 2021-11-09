package test.external.dummy;

import controller.IApplicationEvent;
import controller.GeneralEvent;
import controller.handler.IApplicationEventHandler;
import server.controller.IServerController;
import server.model.IServerModel;

public class DummyServerController implements IServerController {
	public DummyServerController() {
		
	}
	public void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler) {}
	public void handleApplicationEvent(IApplicationEvent event, Object[] args) {
		if (event == GeneralEvent.DEVICE_CONNECTED) {
			this.DeviceConnected((String) args[0]);
		}
		if (event == GeneralEvent.DEVICE_DISCONNECTED) {
			this.DeviceDisconnected((String) args[0]);
		}
		if (event == GeneralEvent.ADD_ORDER) {
			this.addOrder(null);
		}
	}
	public void DeviceConnected(String arg) {
		
	}
	public void DeviceDisconnected(String arg) {
		
	}
	@Override
	public IServerModel getModel() {
		return null;
	}
	public void addOrder(String serialisedOrder) {
		
	}
}
