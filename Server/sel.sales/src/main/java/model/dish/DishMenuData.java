package model.dish;

import java.util.ArrayList;
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

	@Override
	public boolean isEmpty() {
		return dishMenuItems.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IDishMenuData)) {
			return false;
		}
		IDishMenuData castedO = (IDishMenuData) o;
		return this.dishMenuItems.containsAll(castedO.getDishMenuItemCol()) && castedO.getDishMenuItemCol().containsAll(dishMenuItems);
	}

	@Override
	public Collection<IDishMenuItemData> getDishMenuItemCol() {
		Collection<IDishMenuItemData> col = new ArrayList<IDishMenuItemData>();
		this.dishMenuItems.forEach(i -> col.add(i));
		return col;
	}
}
