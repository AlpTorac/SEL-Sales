package model.order;

public class OrderIDFactory implements IOrderIDFactory {

	@Override
	public OrderID createOrderID() {
		return null;
	}

	@Override
	public OrderID createOrderID(String id) {
		return new OrderID(id);
	}

}
