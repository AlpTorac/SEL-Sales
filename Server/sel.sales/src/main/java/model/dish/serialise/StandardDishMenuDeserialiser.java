package model.dish.serialise;

import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemDataFactory;
import model.id.FixIDFactory;

public class StandardDishMenuDeserialiser extends DishMenuDeserialiser {
	public StandardDishMenuDeserialiser() {
		super();
		
		this.format = new IntraAppDishMenuFormat();
		this.menuItemDataFac = new DishMenuItemDataFactory();
		this.menuDataFac = new DishMenuDataFactory(menuItemDataFac);
		
		this.menuParser = new DishMenuParser(
				this.format,
				this.menuDataFac,
				new FixIDFactory());
	}
}
