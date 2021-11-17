package dummyclient;

import controller.Controller;
import controller.GeneralEvent;
import controller.handler.AddDiscoveredDeviceHandler;
import controller.handler.AddKnownDeviceHandler;
import controller.handler.AddOrderHandler;
import controller.handler.AllowKnownDeviceHandler;
import controller.handler.BlockKnownDeviceHandler;
import controller.handler.DeviceConnectedHandler;
import controller.handler.DeviceDisconnectedHandler;
import controller.handler.DiscoverDevicesHandler;
import controller.handler.RemoveKnownDeviceHandler;
import controller.handler.RemoveOrderHandler;
import controller.manager.ApplicationEventManager;
import controller.manager.IApplicationEventManager;
import model.IModel;

public class ClientController extends Controller {

	public ClientController(IModel model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IApplicationEventManager initEventManager() {
		ApplicationEventManager bem = new ApplicationEventManager();
		bem.addApplicationEventToHandlerMapping(GeneralEvent.ADD_ORDER, new AddOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.REMOVE_ORDER, new RemoveOrderHandler(this));
		
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DISCOVER_DEVICES, new DiscoverDevicesHandler(this));
		
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_ADDED, new AddKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_REMOVED, new RemoveKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_ALLOWED, new AllowKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_BLOCKED, new BlockKnownDeviceHandler(this));
		
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_DISCOVERED, new AddDiscoveredDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_CONNECTED, new DeviceConnectedHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_DISCONNECTED, new DeviceDisconnectedHandler(this));
		return bem;
	}

}
