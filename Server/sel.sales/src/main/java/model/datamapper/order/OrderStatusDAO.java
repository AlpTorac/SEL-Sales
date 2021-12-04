package model.datamapper.order;

import model.order.OrderStatus;

public class OrderStatusDAO extends OrderAttributeDAO {
	@Override
	protected String serialiseNotNullValue(Object attributeValue) {
		return ((OrderStatus) attributeValue).getSerialisedVersion();
	}

	@Override
	protected OrderStatus parseNotNullSerialisedValue(String serialisedValue) {
		return OrderStatus.stringToOrderStatus(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedAttribute() {
		return OrderAttribute.STATUS;
	}
}
