package model.dish.serialise;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.entity.id.EntityIDFactory;

public class DishMenuParser implements IDishMenuParser {
	private IDishMenuFormat format;
	private IDishMenuDataFactory menuDataFac;
	private EntityIDFactory idFac;
	
	public DishMenuParser(IDishMenuFormat format, IDishMenuDataFactory menuDataFac, EntityIDFactory idFac) {
		this.format = format;
		this.menuDataFac = menuDataFac;
		this.idFac = idFac;
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

	@Override
	public EntityIDFactory getIDFactory() {
		return this.idFac;
	}

}
