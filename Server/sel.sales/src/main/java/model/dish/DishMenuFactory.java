package model.dish;

public class DishMenuFactory implements IDishMenuFactory {

	@Override
	public IDishMenu createDishMenu() {
		return new DishMenu();
	}

}
