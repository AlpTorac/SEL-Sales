package model.order;

public class OrderStatusSetter extends OrderAttributeSetter {

	@Override
	protected void setOrderAttributeAlgorithm(IOrderCollector orderCollector, String orderID, String serialisedValue) {
		orderCollector.editOrderStatus(orderID, OrderStatus.stringToOrderStatus(serialisedValue));
	}

}
