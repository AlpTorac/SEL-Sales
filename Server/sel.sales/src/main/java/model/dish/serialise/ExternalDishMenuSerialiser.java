package model.dish.serialise;

public class ExternalDishMenuSerialiser extends DishMenuSerialiser {
	public ExternalDishMenuSerialiser() {
		super(new ExternalDishMenuItemSerialiser(), new ExternalDishMenuFormat(), false);
	}
}
