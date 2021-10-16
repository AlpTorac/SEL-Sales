package model.dish.serialise;

import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public abstract class DishMenuDeserialiser implements IDishMenuItemDeserialiser {
	protected IDishMenuParser menuParser;
	
	protected IDishMenuFormat format;
	protected IDishMenuDataFactory menuDataFac;
	protected IDishMenuItemDataFactory menuItemDataFac;
	
	public DishMenuDeserialiser() {
		
	}
	
	@Override
	public IDishMenuItemData deserialise(String serialisedMenuItemData) {
		return this.menuParser.parseDishMenuItemData(serialisedMenuItemData);
	}
}
