package model.order.serialise;

import model.order.IOrderCollector;

public class OrderTableNumberSerialiser extends OrderAttributeSerialiser {
	@Override
	protected String serialiseOrderAttribute(IOrderCollector orderCollector, String orderID) {
		return String.valueOf(orderCollector.getTableNumber(orderID));
	}
}
