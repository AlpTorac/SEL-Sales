package model.order;

import model.IDishMenuItemFinder;

public class OrderFactory implements IOrderFactory {
	private IOrderItemFactory fac;
	
	public OrderFactory(IOrderItemFactory fac) {
		this.fac = fac;
	}
	
	@Override
	public Order createOrder(IDishMenuItemFinder finder, IOrderData data) {
		Order order = new Order(data.getDate(), data.getCashOrCard(), data.getHereOrToGo(), data.getID());
		order.setOrderDiscount(data.getOrderDiscount());
		for (IOrderItemData d : data.getOrderedItems()) {
			order.addOrderItem(this.fac.createOrderItem(finder, d));
		}
		return order;
	}
}
