package model.dish.serialise;

public class IntraAppDishMenuFormat extends DishMenuFormat {
	public IntraAppDishMenuFormat() {
		super(";"+System.lineSeparator(), new IntraAppDishMenuItemFormat());
	}
}
