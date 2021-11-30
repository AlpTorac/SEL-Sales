package model.order.datamapper;

import model.order.OrderStatus;

public class OrderStatusGetter extends OrderAttributeGetter {
	@Override
	protected String serialiseValue(Object attributeValue) {
		return ((OrderStatus) attributeValue).getSerialisedVersion();
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.STATUS;
	}
}
