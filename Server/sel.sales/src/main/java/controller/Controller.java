package controller;

import model.IDishMenuItem;
import model.IDishMenuItemID;
import model.IDishMenuItemIDFactory;
import model.IDishMenuItemData;
import model.IDishMenuItemDataFactory;
import model.IModel;

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
		IDishMenuItem item = this.model.getItem(id);
		return this.dataFac.menuItemToData(item);
	}
	
	public IDishMenuItemDataFactory getItemDataCommunicationProtocoll() {
		return this.dataFac;
	}
	public IDishMenuItemIDFactory getItemIDCommunicationProtocoll() {
		return this.idFac;
	}
}
