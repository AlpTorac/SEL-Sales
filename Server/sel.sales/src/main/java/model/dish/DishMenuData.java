package model.dish;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class DishMenuData implements IDishMenuData {
	private Collection<IDishMenuItemData> dishMenuItems = new CopyOnWriteArrayList<IDishMenuItemData>();
	
	DishMenuData(IDishMenuItemData[] dishMenuItems) {
		for (IDishMenuItemData d : dishMenuItems) {
			this.dishMenuItems.add(d);
		}
	}
	
	@Override
	public IDishMenuItemData[] getAllDishMenuItems() {
		return this.dishMenuItems.toArray(IDishMenuItemData[]::new);
	}

}
