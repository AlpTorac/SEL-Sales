package model.datamapper.order;

public class OrderTableNumberDAO extends OrderAttributeDAO {
	protected OrderTableNumberDAO(String fileAddress, String defaultFileName) {
		super(fileAddress, defaultFileName);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.TABLE_NUMBER;
	}

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseInteger((Integer) attributeValue);
	}

	@Override
	protected Integer parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseInteger(serialisedValue);
	}
}
