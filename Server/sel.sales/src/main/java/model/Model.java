package model;

public class Model implements IModel {
	private IDishMenu dishMenu;
	private IDishMenuItemFactory fac;
	private IDishMenuItemDataFactory dataFac;
	private IDishMenuItemIDFactory idFac;
	
	public Model() {
		this.dishMenu = new DishMenu();
		this.fac = new DishMenuItemFactory();
		this.dataFac = new DishMenuItemDataFactory();
		this.idFac = new DishMenuItemIDFactory();
	}
	
	public void addMenuItem(IDishMenuItemData item) {
		this.dishMenu.addMenuItem(this.fac.createMenuItem(item));
	}

	public void removeMenuItem(IDishMenuItemID id) {
		this.dishMenu.removeMenuItem(id);
	}

	public IDishMenuItem getItem(IDishMenuItemID id) {
		return this.dishMenu.getItem(id);
	}

	@Override
	public IDishMenuItemDataFactory getItemDataCommunicationProtocoll() {
		return this.dataFac;
	}

	@Override
	public IDishMenuItemIDFactory getItemIDCommunicationProtocoll() {
		return this.idFac;
	}
}
