package model.dish;

import model.entity.id.EntityID;

public interface IDishMenuItemFinder {
	DishMenuItem getMenuItem(EntityID id);
	DishMenuItem getMenuItem(String id);
	default DishMenuItemData getMenuItemData(EntityID id) {
		DishMenuItem item = this.getMenuItem(id);
		if (item != null) {
			return item.toData();
		}
		return null;
	}
	default DishMenuItemData getMenuItemData(String id) {
		DishMenuItem item = this.getMenuItem(id);
		if (item != null) {
			return item.toData();
		}
		return null;
	}
}
