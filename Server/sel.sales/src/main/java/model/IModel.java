package model;

public interface IModel {
	void addMenuItem(IDishMenuItemData item);
	void removeMenuItem(IDishMenuItemID item);
	void subscribe(Updatable updatable);
	IDishMenuItem getItem(IDishMenuItemID id);
	IDishMenuItemDataFactory getItemDataCommunicationProtocoll();
	IDishMenuItemIDFactory getItemIDCommunicationProtocoll();
	IDishMenuItemData[] getMenuData();
}
