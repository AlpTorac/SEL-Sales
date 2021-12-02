package model.datamapper;

public class OrderTableNumberSetter extends OrderAttributeSetter {
	@Override
	protected Object parseSerialisedValue(String serialisedValue) {
		return this.parseInteger(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.TABLE_NUMBER;
	}
}
