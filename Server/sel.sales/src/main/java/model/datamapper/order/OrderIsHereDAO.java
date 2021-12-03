package model.datamapper.order;

public class OrderIsHereDAO extends OrderAttributeDAO {

	protected OrderIsHereDAO(String fileAddress, String defaultFileName) {
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
		return OrderAttribute.IS_HERE;
	}
}
