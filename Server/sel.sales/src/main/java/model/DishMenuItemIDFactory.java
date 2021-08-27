package model;

public class DishMenuItemIDFactory implements IDishMenuItemIDFactory {

	public DishMenuItemID createDishMenuItemID() {
		return null;
	}

	@Override
	public DishMenuItemID createDishMenuItemID(String id) {
		return new DishMenuItemID(id);
	}
	
}
