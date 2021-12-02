package model.order;

import java.time.LocalDateTime;

import model.datamapper.OrderAttribute;
import model.entity.IFactory;
import model.entity.id.EntityID;

public class OrderFactory implements IFactory<OrderAttribute, Order, OrderData> {

	@Override
	public OrderData entityToValue(Order entity) {
		OrderData data = this.constructData(
				entity.getID(),
				(LocalDateTime) entity.getAttributeValue(OrderAttribute.DATE),
				(Boolean) entity.getAttributeValue(OrderAttribute.IS_CASH),
				(Boolean) entity.getAttributeValue(OrderAttribute.IS_HERE));
		
		data.getOrderItemAggregate().addAll(entity.getOrderItemAggregate());
		return data;
	}
	
	public Order constructOrder(EntityID id, LocalDateTime date, boolean isCash, boolean isHere) {
		Order order = new Order(id);
		order.setAttributeValue(OrderAttribute.DATE, date);
		order.setAttributeValue(OrderAttribute.IS_CASH, isCash);
		order.setAttributeValue(OrderAttribute.IS_HERE, isHere);
		return order;
	}
	
	public OrderData constructData(EntityID id, LocalDateTime date, boolean isCash, boolean isHere) {
		OrderData data = new OrderData(id);
		data.setAttributeValue(OrderAttribute.DATE, date);
		data.setAttributeValue(OrderAttribute.IS_CASH, isCash);
		data.setAttributeValue(OrderAttribute.IS_HERE, isHere);
		return data;
	}
}
