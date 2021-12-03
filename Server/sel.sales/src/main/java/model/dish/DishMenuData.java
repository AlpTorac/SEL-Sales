package model.dish;

import model.datamapper.DishMenuItemAttribute;
import model.entity.Aggregate;

public class DishMenuData extends Aggregate<DishMenuItemAttribute, DishMenuItemData> {

	@Override
	public Aggregate<DishMenuItemAttribute, DishMenuItemData> getEmptyClone() {
		return new DishMenuData();
	}
	
	public DishMenu toDishMenu() {
		DishMenu menu = new DishMenu();
		for (DishMenuItemData data : this.getAllItems()) {
			menu.addElement(data);
		}
		return menu;
	}
}
