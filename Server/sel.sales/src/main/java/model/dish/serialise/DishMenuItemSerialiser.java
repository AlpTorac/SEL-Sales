package model.dish.serialise;

public class DishMenuItemSerialiser implements IDishMenuItemSerialiser {
	private IDishMenuFormat dishMenuFormat = new DishMenuFormat();
	@Override
	public IDishMenuFormat getDishMenuFormat() {
		return this.dishMenuFormat;
	}

}
