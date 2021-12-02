package model.datamapper;

public class OrderNoteGetter extends OrderAttributeGetter {
	@Override
	protected String serialiseValue(Object attributeValue) {
		return (String) attributeValue;
	}

	@Override
	protected IAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.NOTE;
	}
}
