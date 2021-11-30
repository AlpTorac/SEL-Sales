package model.order.datamapper;

public class OrderWrittenSetter extends OrderAttributeSetter {

	@Override
	protected Object parseSerialisedValue(String serialisedValue) {
		return this.parseBoolean(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.IS_WRITTEN;
	}

}
