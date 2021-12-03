package model.dish;

import model.entity.id.EntityID;

public interface IDishMenuItemFinder {
	DishMenuItem getMenuItem(EntityID id);
	DishMenuItem getMenuItem(String id);
	default DishMenuItemData getMenuItemData(EntityID id) {
		return this.getMenuItem(id).toData();
	}
	default DishMenuItemData getMenuItemData(String id) {
		return this.getMenuItem(id).toData();
	}
}
