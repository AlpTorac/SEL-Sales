package model;

import model.dish.IDishMenu;
import model.dish.IDishMenuItemData;

public class DishMenuItemFinder implements IDishMenuItemFinder {
	private IDishMenu menu;
	
	DishMenuItemFinder(IDishMenu menu) {
		this.menu = menu;
	}
	
	@Override
	public IDishMenuItemData getDish(String id) {
		return this.menu.getItem(id);
	}

}
