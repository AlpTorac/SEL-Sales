package model;

import java.util.ArrayList;
import java.util.Collection;

import model.dish.DishMenu;
import model.dish.DishMenuItemDataFactory;
import model.dish.DishMenuItemFactory;
import model.dish.DishMenuItemIDFactory;
import model.dish.IDishMenu;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	private IDishMenu dishMenu;
	private IDishMenuItemFactory fac;
	private IDishMenuItemDataFactory dataFac;
	private IDishMenuItemIDFactory idFac;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.dishMenu = new DishMenu();
		this.fac = new DishMenuItemFactory();
		this.dataFac = new DishMenuItemDataFactory();
		this.idFac = new DishMenuItemIDFactory();
	}
	
	public void addMenuItem(IDishMenuItemData item) {
		if (this.dishMenu.addMenuItem(this.fac.createMenuItem(item))) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
	}

	public void removeMenuItem(IDishMenuItemID id) {
		if (this.dishMenu.removeMenuItem(id)) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
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

	@Override
	public IDishMenuItemData[] getMenuData() {
		IDishMenuItem[] items = this.dishMenu.getAllItems();
		IDishMenuItemData[] data = new IDishMenuItemData[items.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.dataFac.menuItemToData(items[i]);
		}
		return data;
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}
}
