package model.datamapper;

public class OrderNoteSetter extends OrderAttributeSetter {
	@Override
	protected Object parseSerialisedValue(String serialisedValue) {
		return serialisedValue;
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.NOTE;
	}
}
