package model.order.datamapper;

public class OrderTableNumberGetter extends OrderAttributeGetter {

	@Override
	protected String serialiseValue(Object attributeValue) {
		return String.valueOf(((Number) attributeValue).intValue());
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.TABLE_NUMBER;
	}
	
}
