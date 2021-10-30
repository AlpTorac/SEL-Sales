package model.order;

public class OrderCollectorFactory implements IOrderCollectorFactory {
	@Override
	public IOrderCollector createOrderCollector() {
		return new OrderCollector();
	}
}
