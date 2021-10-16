package model.dish.serialise;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;

public class DishMenuParser implements IDishMenuParser {
	private IDishMenuFormat format;
	private IDishMenuDataFactory menuDataFac;
	
	public DishMenuParser(IDishMenuFormat format, IDishMenuDataFactory menuDataFac) {
		this.format = format;
		this.menuDataFac = menuDataFac;
	}
	
	@Override
	public IDishMenuFormat getDishMenuFormat() {
		return this.format;
	}

	@Override
	public IDishMenuItemDataFactory getDishMenuItemDataFactory() {
		return this.menuDataFac.getItemDataFac();
	}

	@Override
	public IDishMenuDataFactory getDishMenuFactory() {
		return this.menuDataFac;
	}

}
