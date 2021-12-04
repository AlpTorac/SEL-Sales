package model.datamapper.menu;

public class DishMenuItemDishNameDAO extends DishMenuItemAttributeDAO {

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseString((String) attributeValue);
	}

	@Override
	protected String parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseString(serialisedValue);
	}

	@Override
	protected DishMenuItemAttribute getAssociatedAttribute() {
		return DishMenuItemAttribute.DISH_NAME;
	}

}
