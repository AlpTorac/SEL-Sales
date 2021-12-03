package model.datamapper;

import java.math.BigDecimal;

public enum DishMenuItemAttribute implements IAttribute {
	DISH_NAME("dishName", ""),
	PORTION_SIZE("portionSize", BigDecimal.ONE),
	GROSS_PRICE("grossPrice", BigDecimal.ZERO),
	PRODUCTION_COST("productionCost", BigDecimal.ZERO)
	;

	private String description;
	private Object defaultValue;
	
	private DishMenuItemAttribute(String description, Object defaultValue) {
		this.description = description;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public IAttribute[] getAllAttributes() {
		return DishMenuItemAttribute.values();
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

}
