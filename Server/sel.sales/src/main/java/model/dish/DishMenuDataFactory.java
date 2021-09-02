package model.dish;

public class DishMenuDataFactory implements IDishMenuDataFactory {

	@Override
	public DishMenuData constructData(IDishMenuItemData[] dishMenuItems) {
		return new DishMenuData(dishMenuItems);
	}

}
