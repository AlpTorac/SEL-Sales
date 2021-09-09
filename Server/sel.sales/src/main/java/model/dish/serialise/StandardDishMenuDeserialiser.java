package model.dish.serialise;

import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemDataFactory;

public class StandardDishMenuDeserialiser extends DishMenuDeserialiser {
	public StandardDishMenuDeserialiser() {
		super();
		
		this.format = new IntraAppDishMenuFormat();
		this.menuItemfDataFac = new DishMenuItemDataFactory();
		this.menuDataFac = new DishMenuDataFactory();
		
		this.menuParser = new DishMenuParser(
				this.format,
				this.menuItemfDataFac,
				this.menuDataFac);
	}
}
