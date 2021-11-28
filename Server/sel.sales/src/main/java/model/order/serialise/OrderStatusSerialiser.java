package model.order.serialise;

import model.order.IOrderCollector;

public class OrderStatusSerialiser extends OrderAttributeSerialiser {
	@Override
	protected String serialiseOrderAttribute(IOrderCollector orderCollector, String orderID) {
		return orderCollector.getOrderStatus(orderID).getSerialisedVersion();
	}
}
