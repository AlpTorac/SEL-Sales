package model.order.datamapper;

public class OrderWrittenGetter extends OrderAttributeGetter {

	@Override
	protected String serialiseValue(Object attributeValue) {
		return this.serialiseBoolean(((Boolean) attributeValue));
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.IS_WRITTEN;
	}

}
