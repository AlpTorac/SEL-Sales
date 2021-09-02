package model.order;

import java.time.LocalDateTime;

public class OrderDataFactory implements IOrderDataFactory {
	private IOrderItemDataFactory orderItemDatafac;
	public OrderDataFactory(IOrderItemDataFactory orderItemDatafac) {
		this.orderItemDatafac = orderItemDatafac;
	}
	
	@Override
	public OrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean cashOrCard,
			boolean hereOrToGo, String id) {
		return new OrderData(orderData, date, cashOrCard, hereOrToGo, id);
	}

	@Override
	public OrderData orderToData(IOrder order) {
		OrderData data = new OrderData(
				order.getOrderItemCollection().stream().map((o) -> {return orderItemDatafac.orderItemToData(o);}).toArray(IOrderItemData[]::new),
				order.getDate(),
				order.getCashOrCard(),
				order.getHereOrToGo(),
				order.getID());
		
		data.setOrderDiscount(order.getOrderDiscount());
		
		return data;
	}

}
