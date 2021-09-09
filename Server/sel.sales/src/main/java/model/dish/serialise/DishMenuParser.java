package model.dish.serialise;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;

public class DishMenuParser implements IDishMenuParser {
	private IDishMenuItemFormat format;
	private IDishMenuItemDataFactory menuItemDataFac;
	private IDishMenuDataFactory menuDataFac;
	
	DishMenuParser(IDishMenuItemFormat format, IDishMenuItemDataFactory menuItemDataFac, IDishMenuDataFactory menuDataFac) {
		this.format = format;
		this.menuItemDataFac = menuItemDataFac;
		this.menuDataFac = menuDataFac;
	}
	
	@Override
	public IDishMenuItemFormat getDishMenuFormat() {
		return this.format;
	}

	@Override
	public IDishMenuItemDataFactory getDishMenuItemDataFactory() {
		return this.menuItemDataFac;
	}

	@Override
	public IDishMenuDataFactory getDishMenuFactory() {
		return this.menuDataFac;
	}

}
