package model;

import java.util.Set;
import java.util.TreeSet;

public class DishMenu implements IDishMenu {
	
	private Set<IDishMenuItem> dishes;
	
	DishMenu() {
		this.dishes = new TreeSet<IDishMenuItem>();
	}
	
	@Override
	public boolean addMenuItem(IDishMenuItem item) {
		return this.dishes.add(item);
	}
	
	@Override
	public boolean removeMenuItem(IDishMenuItemID id) {
		return this.dishes.removeIf(item -> item.getID().equals(id));
	}
	
	@Override
	public IDishMenuItem getItem(IDishMenuItemID id) {
		IDishMenuItem soughtItem = this.dishes.stream()
				.filter(item -> item.getID().equals(id))
				.findFirst().get();
		return soughtItem;
	}

	@Override
	public IDishMenuItem[] getAllItems() {
		return this.dishes.toArray(IDishMenuItem[]::new);
	}
	
}