package model.dish.serialise;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;

public class DishMenuParser implements IDishMenuParser {
	private IDishMenuFormat format;
	private IDishMenuItemDataFactory menuItemDataFac;
	private IDishMenuDataFactory menuDataFac;
	
	public DishMenuParser(IDishMenuFormat format, IDishMenuItemDataFactory menuItemDataFac, IDishMenuDataFactory menuDataFac) {
		this.format = format;
		this.menuItemDataFac = menuItemDataFac;
		this.menuDataFac = menuDataFac;
	}
	
	@Override
	public IDishMenuFormat getDishMenuFormat() {
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
