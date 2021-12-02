package model.dish;

import model.entity.id.EntityID;
import model.entity.id.MinimalIDFactory;

public class DishMenuItemFinder implements IDishMenuItemFinder {
	private DishMenu menu;
	private MinimalIDFactory fac;
	
	public DishMenuItemFinder(DishMenu menu) {
		this.menu = menu;
		this.fac = new MinimalIDFactory();
	}
	
	@Override
	public DishMenuItem getDish(EntityID id) {
		return this.menu.getElement(id);
	}

	@Override
	public DishMenuItem getDish(String id) {
		return this.menu.getElement(this.fac.createID(id));
	}

}
