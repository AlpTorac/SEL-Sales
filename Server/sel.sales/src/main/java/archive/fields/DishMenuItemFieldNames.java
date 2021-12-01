package archive.fields;

public enum DishMenuItemFieldNames implements IFieldNameEnum {
	DISH_NAME("dishName"),
	DISH_ID("dishID"),
	PORTION_SIZE("portionSize"),
	PRODUCTION_COST("productionCost"),
	PRICE("price")
	;
	
	private String fieldName;
	
	DishMenuItemFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public IFieldNameEnum[] getValues() {
		return DishMenuItemFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}

}
