package model.order.datamapper;

public class OrderWrittenGetter extends OrderAttributeGetter {

	@Override
	protected String serialiseValue(Object attributeValue) {
		if (attributeValue == null) {
			return this.serialiseBoolean(false);
		}
		return this.serialiseBoolean(((Boolean) attributeValue));
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.IS_WRITTEN;
	}

}
