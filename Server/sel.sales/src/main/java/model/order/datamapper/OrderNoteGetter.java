package model.order.datamapper;

public class OrderNoteGetter extends OrderAttributeGetter {
	@Override
	protected String serialiseValue(Object attributeValue) {
		return (String) attributeValue;
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.NOTE;
	}
}
