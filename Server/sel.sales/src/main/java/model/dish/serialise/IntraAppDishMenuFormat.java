package model.dish.serialise;

public class IntraAppDishMenuFormat extends DishMenuFormat {
	protected IntraAppDishMenuFormat() {
		super(";", new IntraAppDishMenuItemFormat());
	}
}
