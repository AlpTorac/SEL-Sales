package model.order;

public class OrderFactory implements IOrderFactory {
	public OrderFactory() {

	}
	
	@Override
	public Order createOrder(IOrderData data) {
		Order order = new Order(data.getDate(), data.getIsCash(),
				data.getIsHere(), data.getID());
		for (IOrderItemData d : data.getOrderedItems()) {
			order.addOrderItem(d);
		}
		return order;
	}
}
