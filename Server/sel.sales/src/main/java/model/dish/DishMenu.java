package model.dish;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class DishMenu implements IDishMenu {
	
	private Map<IDishMenuItemID, IDishMenuItem> dishes;
	
	public DishMenu() {
		this.dishes = new ConcurrentSkipListMap<IDishMenuItemID, IDishMenuItem>();
	}
	
	/**
	 * @return True, if item has been added. False, if the id is already taken.
	 */
	@Override
	public boolean addMenuItem(IDishMenuItem item) {
		IDishMenuItemID id = item.getID();
		
		if (!this.dishes.containsKey(id)) {
			return this.dishes.put(id, item) == null;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean removeMenuItem(IDishMenuItemID id) {
		return this.dishes.remove(id) != null;
	}
	
	@Override
	public IDishMenuItem getItem(IDishMenuItemID id) {
		return this.dishes.get(id);
	}

	@Override
	public IDishMenuItem[] getAllItems() {
		return this.dishes.values().toArray(IDishMenuItem[]::new);
	}
	
}