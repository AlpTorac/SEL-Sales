package model.datamapper.menu;

import java.math.BigDecimal;

public class DishMenuItemPortionSizeDAO extends DishMenuItemAttributeDAO {

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseBigDecimal((BigDecimal) attributeValue);
	}

	@Override
	protected BigDecimal parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseBigDecimal(serialisedValue);
	}

	@Override
	protected DishMenuItemAttribute getAssociatedAttribute() {
		return DishMenuItemAttribute.PORTION_SIZE;
	}
}