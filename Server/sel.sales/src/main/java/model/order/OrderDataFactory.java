package model.order;

import java.time.LocalDateTime;

import model.id.EntityID;
import model.id.EntityIDFactory;

public class OrderDataFactory implements IOrderDataFactory {
	private IOrderItemDataFactory orderItemDatafac;
	private EntityIDFactory idFac;
	
	public OrderDataFactory(IOrderItemDataFactory orderItemDatafac, EntityIDFactory idFac) {
		this.orderItemDatafac = orderItemDatafac;
		this.idFac = idFac;
	}
	
	@Override
	public OrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash,
			boolean isHere, EntityID id) {
		return new OrderData(orderData, date, isCash, isHere, id);
	}

	@Override
	public OrderData orderToData(IOrder order) {
		OrderData data = new OrderData(
				order.getOrderItemCollection().stream().map((o) -> {return orderItemDatafac.orderItemToData(o);}).toArray(IOrderItemData[]::new),
				order.getDate(),
				order.getIsCash(),
				order.getIsHere(),
				order.getID());
		
		return data;
	}

	@Override
	public IOrderItemDataFactory getItemDataFac() {
		return this.orderItemDatafac;
	}

	@Override
	public IOrderData constructData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere,
			Object... idParameters) {
		return new OrderData(orderData, date, isCash, isHere, this.idFac.createID(idParameters));
	}

}
