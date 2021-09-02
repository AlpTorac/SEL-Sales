package model.order;

public class OrderItemFactory implements IOrderItemFactory {
	public OrderItemFactory() {
		
	}
	
	@Override
	public OrderItem createOrderItem(IOrderItemData item) {
		return new OrderItem(item);
	}
}
