package model.order;

public class OrderFactory implements IOrderFactory {
	private IOrderIDFactory idFac;
	public OrderFactory(IOrderIDFactory idFac) {
		this.idFac = idFac;
	}
	
	@Override
	public Order createOrder(IOrderData data) {
		Order order = new Order(data.getDate(), data.getIsCash(), data.getIsHere(), this.idFac.createOrderID(data.getID()));
		for (IOrderItemData d : data.getOrderedItems()) {
			order.addOrderItem(d);
		}
		return order;
	}
}
