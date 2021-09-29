package controller;

import controller.handler.IApplicationEventHandler;
import controller.manager.IApplicationEventManager;
import model.IModel;
import model.connectivity.IClientData;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

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
	
	protected abstract IApplicationEventManager initEventManager();
	
	public void addMenuItem(String serialisedItemData) {
		this.model.addMenuItem(serialisedItemData);
	}
	public void removeMenuItem(String id) {
		this.model.removeMenuItem(id);
	}
	public IDishMenuItemData getItem(String id) {
		IDishMenuItemData item = this.model.getMenuItem(id);
		return item;
	}
	
	public void addOrder(String serialisedOrder) {
		this.model.addUnconfirmedOrder(serialisedOrder);
	}
	
	public void confirmOrder(String serialisedConfirmedOrderData) {
		this.model.confirmOrder(serialisedConfirmedOrderData);
	}
	
	public void removeOrder(String id) {
		this.model.removeUnconfirmedOrder(id);
		this.model.removeConfirmedOrder(id);
	}
	
	public void addKnownClient(IClientData clientData) {
		this.model.addKnownClient(clientData);
	}
	
	@Override
	public void editMenuItem(String serialisedNewItemData) {
		this.model.editMenuItem(serialisedNewItemData);
	}
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {
		return this.model.getDishMenuItemSerialiser();
	}
	public IOrderSerialiser getOrderSerialiser() {
		return this.model.getOrderSerialiser();
	}
}
