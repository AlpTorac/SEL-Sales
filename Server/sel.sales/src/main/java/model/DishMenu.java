package model;

import java.util.HashMap;
import java.util.Map;

public class DishMenu {
	
	private Map<DishMenuItemID, DishMenuItem> dishes;
	
	DishMenu() {
		this.dishes = new HashMap<DishMenuItemID, DishMenuItem>();
	}
	
	public void addMenuItem(DishMenuItem item) {
		this.dishes.put(item.getID(), item);
	}
	
	public void removeMenuItem(DishMenuItem item) {
		this.dishes.remove(item.getID());
	}
	
	public DishMenuItem getItem(final DishMenuItemID id) {
		return this.dishes.get(id);
	}
	
}