package model.dish.serialise;

public class ExternalDishMenuFormat extends DishMenuFormat {
	public ExternalDishMenuFormat() {
		super(";", new ExternalDishMenuItemFormat());
	}
}
