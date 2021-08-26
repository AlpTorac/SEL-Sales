package model;

public interface IModel {
	void addMenuItem(IDishMenuItemData item);
	void removeMenuItem(IDishMenuItemID item);
	IDishMenuItem getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
}
