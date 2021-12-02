package model.datamapper;

public class OrderWrittenGetter extends OrderAttributeGetter {

	@Override
	protected String serialiseValue(Object attributeValue) {
		if (attributeValue == null) {
			return this.serialiseBoolean(false);
		}
		return this.serialiseBoolean(((Boolean) attributeValue));
	}

	@Override
	protected IAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.IS_WRITTEN;
	}

}
