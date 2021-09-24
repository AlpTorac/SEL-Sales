package model.dish.serialise;

public class ExternalDishMenuFormat extends DishMenuFormat {
	protected ExternalDishMenuFormat() {
		super(";", new ExternalDishMenuItemFormat());
	}
}
