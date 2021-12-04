package model.dish;

import model.datamapper.menu.DishMenuItemAttribute;
import model.entity.IFactory;
import model.entity.Repository;

public class DishMenu extends Repository<DishMenuItemAttribute, DishMenuItem, DishMenuItemData> {
	public DishMenu() {
		
	}
	
	@Override
	protected IFactory<DishMenuItemAttribute, DishMenuItem, DishMenuItemData> getDefaultFactory() {
		return new DishMenuItemFactory();
	}
	
	public DishMenuData toData() {
		DishMenuData data = new DishMenuData();
		for (DishMenuItem item : this.getAllElements()) {
			data.addElement(this.toValueObject(item));
		}
		return data;
	}
}