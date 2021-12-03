package model.datamapper.order;

public class OrderIsCashDAO extends OrderAttributeDAO {

	protected OrderIsCashDAO(String fileAddress, String defaultFileName) {
		super(fileAddress, defaultFileName);
	}

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseBoolean((Boolean) attributeValue);
	}

	@Override
	protected Boolean parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseBoolean(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.IS_CASH;
	}
}
