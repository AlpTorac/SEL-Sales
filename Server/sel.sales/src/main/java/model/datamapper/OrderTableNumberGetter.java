package model.datamapper;

public class OrderTableNumberGetter extends OrderAttributeGetter {

	@Override
	protected String serialiseValue(Object attributeValue) {
		if (attributeValue == null) {
			return "-1";
		}
		return String.valueOf(((Number) attributeValue).intValue());
	}

	@Override
	protected IAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.TABLE_NUMBER;
	}
	
}
