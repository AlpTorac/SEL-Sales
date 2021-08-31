package controller;

import model.IModel;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderIDFactory;

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
	
	public void addMenuItem(IDishMenuItemData data) {
		this.model.addMenuItem(data);
	}
	public void removeMenuItem(IDishMenuItemID id) {
		this.model.removeMenuItem(id);
	}
	public IDishMenuItemData getItem(IDishMenuItemID id) {
		IDishMenuItem item = this.model.getMenuItem(id);
		return this.model.getItemDataCommunicationProtocoll().menuItemToData(item);
	}
	
	public IDishMenuItemDataFactory getItemDataCommunicationProtocoll() {
		return this.model.getItemDataCommunicationProtocoll();
	}
	public IDishMenuItemIDFactory getItemIDCommunicationProtocoll() {
		return this.model.getItemIDCommunicationProtocoll();
	}
	
	public IOrderDataFactory getOrderDataCommunicationProtocoll() {
		return this.model.getOrderDataCommunicationProtocoll();
	}
	public IOrderIDFactory getOrderItemDataCommunicationProtocoll() {
		return this.model.getOrderItemDataCommunicationProtocoll();
	}
	
	public void addOrder(String serialisedOrder) {
		this.model.addUnconfirmedOrder(serialisedOrder);
	}
	
	public void confirmOrder(IOrderData data) {
		this.model.addConfirmedOrder(data);
	}
}
