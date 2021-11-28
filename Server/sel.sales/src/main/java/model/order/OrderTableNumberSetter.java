package model.order;

public class OrderTableNumberSetter extends OrderAttributeSetter {
	@Override
	protected void setOrderAttributeAlgorithm(IOrderCollector orderCollector, String orderID, String serialisedValue) {
		orderCollector.setTableNumber(orderID, Integer.valueOf(serialisedValue));
	}
}
