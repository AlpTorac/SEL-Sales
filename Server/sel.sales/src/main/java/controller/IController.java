package controller;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

public interface IController {
	void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler);
	void handleBusinessEvent(BusinessEvent event, Object[] args);
	void addMenuItem(IDishMenuItemData args);
	void removeMenuItem(IDishMenuItemID id);
	IDishMenuItemData getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
}
