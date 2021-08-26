package controller;

import model.IDishMenuItemData;
import model.IDishMenuItemDataFactory;
import model.IDishMenuItemID;
import model.IDishMenuItemIDFactory;

public interface IController {
	void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler);
	void handleBusinessEvent(BusinessEvent event, Object[] args);
	void addMenuItem(IDishMenuItemData args);
	void removeMenuItem(IDishMenuItemID id);
	IDishMenuItemData getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
}
