package model.datamapper;

import model.order.OrderStatus;

public class OrderStatusGetter extends OrderAttributeGetter {
	@Override
	protected String serialiseValue(Object attributeValue) {
		return ((OrderStatus) attributeValue).getSerialisedVersion();
	}

	@Override
	protected IAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.STATUS;
	}
}
