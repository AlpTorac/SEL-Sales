package controller;

import model.IModel;
import model.dish.IDishMenuItemData;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.serialise.IOrderSerialiser;

public abstract class Controller implements IController {
	private IBusinessEventManager eventManager;
	private IModel model;
	
	public Controller(IModel model) {
		this.model = model;
		this.eventManager = this.initEventManager();
	}
	
	public void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler) {
		this.eventManager.addBusinessEventToHandlerMapping(event, handler);
	}
	
	public void handleBusinessEvent(BusinessEvent event, Object[] args) {
		this.eventManager.getHandler(event).handleBusinessEvent(args);
	}
	
	protected abstract IBusinessEventManager initEventManager();
	
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
