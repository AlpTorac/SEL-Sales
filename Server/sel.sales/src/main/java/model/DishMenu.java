package model;

import java.util.HashMap;
import java.util.Map;

public class DishMenu implements IDishMenu {
	
	private Map<IDishMenuItemID, IDishMenuItem> dishes;
	
	DishMenu() {
		this.dishes = new HashMap<IDishMenuItemID, IDishMenuItem>();
	}
	
	@Override
	public void addMenuItem(IDishMenuItem item) {
		this.dishes.put(item.getID(), item);
	}
	
	@Override
	public void removeMenuItem(IDishMenuItemID id) {
		this.dishes.remove(id);
	}
	
	@Override
	public IDishMenuItem getItem(IDishMenuItemID id) {
		return this.dishes.get(id);
	}
	
}