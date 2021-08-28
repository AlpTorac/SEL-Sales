package model.order;

import model.IDishMenuItemFinder;

public class OrderItemFactory implements IOrderItemFactory {
	@Override
	public OrderItem createOrderItem(IDishMenuItemFinder finder, IOrderItemData item) {
		return new OrderItem(finder, item);
	}
}
