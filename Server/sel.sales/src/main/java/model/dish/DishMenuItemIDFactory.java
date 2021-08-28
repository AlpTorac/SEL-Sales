package model.dish;

public class DishMenuItemIDFactory implements IDishMenuItemIDFactory {

	@Override
	public DishMenuItemID createDishMenuItemID() {
		return null;
	}

	@Override
	public DishMenuItemID createDishMenuItemID(String id) {
		return new DishMenuItemID(id);
	}
	
}
