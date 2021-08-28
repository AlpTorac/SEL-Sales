package model;

import model.dish.IDishMenu;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemID;

public class DishMenuItemFinder implements IDishMenuItemFinder {
	private IDishMenu menu;
	
	DishMenuItemFinder(IDishMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public IDishMenuItem getDish(IDishMenuItemID id) {
		return this.menu.getItem(id);
	}

}
