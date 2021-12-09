package model.datamapper.order;

public class OrderNoteDAO extends OrderAttributeDAO {
	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.NOTE;
	}

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseString((String) attributeValue);
	}

	@Override
	protected String parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseString(serialisedValue);
	}

}