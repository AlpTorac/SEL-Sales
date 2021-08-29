package controller;

import model.IModel;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

public abstract class Controller implements IController {
	private IBusinessEventManager eventManager;
	private IModel model;
	private IDishMenuItemDataFactory dataFac;
	private IDishMenuItemIDFactory idFac;
	
	public Controller(IModel model) {
		this.model = model;
		this.eventManager = this.initEventManager();
		this.dataFac = this.model.getItemDataCommunicationProtocoll();
		this.idFac = this.model.getItemIDCommunicationProtocoll();
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
		return this.dataFac.menuItemToData(item);
	}
	
	public IDishMenuItemDataFactory getItemDataCommunicationProtocoll() {
		return this.dataFac;
	}
	public IDishMenuItemIDFactory getItemIDCommunicationProtocoll() {
		return this.idFac;
	}
	
	public void addOrder(String serialisedOrder) {
		this.model.addOrder(serialisedOrder);
	}
}
