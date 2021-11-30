package model.order.datamapper;

import model.order.OrderStatus;

public class OrderStatusSetter extends OrderAttributeSetter {

	@Override
	protected Object parseSerialisedValue(String serialisedValue) {
		return OrderStatus.stringToOrderStatus(serialisedValue);
	}

	@Override
	protected OrderAttribute getAssociatedOrderAttribute() {
		return OrderAttribute.STATUS;
	}

}
