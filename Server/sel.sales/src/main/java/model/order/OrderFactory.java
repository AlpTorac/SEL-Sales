package model.order;

import java.time.LocalDateTime;

import model.datamapper.order.OrderAttribute;
import model.entity.IFactory;
import model.entity.id.EntityID;
import model.entity.id.EntityIDFactory;
import model.entity.id.MinimalIDFactory;

public class OrderFactory implements IFactory<OrderAttribute, Order, OrderData> {
	private EntityIDFactory idFac = new MinimalIDFactory();
	@Override
	public OrderData entityToValue(Order entity) {
		OrderData data = this.constructData(
				entity.getID(),
				(LocalDateTime) entity.getAttributeValue(OrderAttribute.DATE),
				(Boolean) entity.getAttributeValue(OrderAttribute.IS_CASH),
				(Boolean) entity.getAttributeValue(OrderAttribute.IS_HERE));
		
		data.getOrderItemAggregate().addAll(entity.getOrderItemAggregate().getOrderedItems());
		return data;
	}
	
	Order constructOrder(EntityID id, LocalDateTime date, boolean isCash, boolean isHere) {
		Order order = this.constructMinimalEntity(id);
		order.setAttributeValue(OrderAttribute.DATE, date);
		order.setAttributeValue(OrderAttribute.IS_CASH, isCash);
		order.setAttributeValue(OrderAttribute.IS_HERE, isHere);
		return order;
	}
	
	Order constructMinimalEntity(EntityID id) {
		Order order = new Order(id);
		order.setAttributeValue(OrderAttribute.ORDER_ITEMS, new AccumulatingOrderItemAggregate());
		return order;
	}
	
	public OrderData constructData(EntityID id, LocalDateTime date, boolean isCash, boolean isHere) {
		OrderData data = this.constructMinimalValueObject(id);
		data.setAttributeValue(OrderAttribute.DATE, date);
		data.setAttributeValue(OrderAttribute.IS_CASH, isCash);
		data.setAttributeValue(OrderAttribute.IS_HERE, isHere);
		return data;
	}
	
	public OrderData constructData(String id, LocalDateTime date, boolean isCash, boolean isHere) {
		return this.constructData(this.idFac.createID(id), date, isCash, isHere);
	}
	
	public OrderData constructMinimalValueObject(String id) {
		return this.constructMinimalValueObject(this.idFac.createID(id));
	}
	
	public OrderData constructMinimalValueObject(EntityID id) {
		OrderData data = new OrderData(id);
		data.setAttributeValue(OrderAttribute.ORDER_ITEMS, new AccumulatingOrderItemAggregate());
		return data;
	}

	@Override
	public Order valueToEntity(OrderData valueObject) {
		Order order = this.constructMinimalEntity(valueObject.getID());
		order.setAttributeValue(OrderAttribute.DATE, valueObject.getAttributeValue(OrderAttribute.DATE));
		order.setAttributeValue(OrderAttribute.IS_CASH, valueObject.getAttributeValue(OrderAttribute.IS_CASH));
		order.setAttributeValue(OrderAttribute.IS_HERE, valueObject.getAttributeValue(OrderAttribute.IS_HERE));
		order.addAllOrderItems(valueObject.getOrderedItems());
		return order;
	}
}
