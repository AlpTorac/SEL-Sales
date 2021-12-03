package controller;

import controller.handler.AddDiscoveredDeviceHandler;
import controller.handler.AddKnownDeviceHandler;
import controller.handler.AddOrderHandler;
import controller.handler.AllowKnownDeviceHandler;
import controller.handler.BlockKnownDeviceHandler;
import controller.handler.DeviceConnectedHandler;
import controller.handler.DeviceDisconnectedHandler;
import controller.handler.DiscoverDevicesHandler;
import controller.handler.IApplicationEventHandler;
import controller.handler.RemoveKnownDeviceHandler;
import controller.handler.RemoveOrderHandler;
import controller.handler.SaveSettingsHandler;
import controller.handler.SettingsChangedHandler;
import controller.manager.ApplicationEventManager;
import controller.manager.IApplicationEventManager;
import model.IModel;

public abstract class Controller implements IController {

	private IApplicationEventManager eventManager;
	private IModel model;

	public Controller(IModel model) {
		this.model = model;
		this.eventManager = this.initEventManager();
	}

	public void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler) {
		this.eventManager.addApplicationEventToHandlerMapping(event, handler);
	}

	public void handleApplicationEvent(IApplicationEvent event, Object[] args) {
		this.eventManager.getHandler(event).handleApplicationEvent(args);
	}

	@Override
	public IModel getModel() {
		return this.model;
	}
	
	protected IApplicationEventManager createEventManager() {
		return new ApplicationEventManager();
	}
	
	protected IApplicationEventManager initEventManager() {
		IApplicationEventManager bem = this.createEventManager();
		bem.addApplicationEventToHandlerMapping(GeneralEvent.ADD_ORDER, new AddOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.REMOVE_ORDER, new RemoveOrderHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DISCOVER_DEVICES, new DiscoverDevicesHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.SAVE_SETTINGS, new SaveSettingsHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_ADDED, new AddKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_REMOVED, new RemoveKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_ALLOWED, new AllowKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.KNOWN_DEVICE_BLOCKED, new BlockKnownDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_DISCOVERED, new AddDiscoveredDeviceHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_CONNECTED, new DeviceConnectedHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.DEVICE_DISCONNECTED, new DeviceDisconnectedHandler(this));
		bem.addApplicationEventToHandlerMapping(GeneralEvent.SETTINGS_CHANGED, new SettingsChangedHandler(this));
		return bem;
	}

}