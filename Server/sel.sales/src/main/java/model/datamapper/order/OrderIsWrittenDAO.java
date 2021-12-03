package model.datamapper.order;

public class OrderIsWrittenDAO extends OrderAttributeDAO {
	protected OrderIsWrittenDAO(String fileAddress, String defaultFileName) {
		super(fileAddress, defaultFileName);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.IS_WRITTEN;
	}

	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return this.serialiseBoolean((Boolean) attributeValue);
	}

	@Override
	protected Boolean parseNotNullSerialisedValue(String serialisedValue) {
		return this.parseBoolean(serialisedValue);
	}
}
