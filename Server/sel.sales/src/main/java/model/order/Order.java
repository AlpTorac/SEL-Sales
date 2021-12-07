package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.entity.Entity;
import model.entity.id.EntityID;

public class Order extends Entity<OrderAttribute> {
//	private AccumulatingOrderItemAggregate orderItems;
	
	protected Order(EntityID id) {
		super(id);
//		this.orderItems = this.initOrderItemAggregate();
	}
	
//	protected AccumulatingOrderItemAggregate initOrderItemAggregate() {
//		return new AccumulatingOrderItemAggregate();
//	}
	
	public void addOrderItem(DishMenuItemData menuItem, BigDecimal amount) {
		this.getOrderItemAggregate().addElement(menuItem, amount);
	}
	
	public void removeOrderItem(EntityID menuItemID, BigDecimal amount) {
		this.getOrderItemAggregate().removeElement(menuItemID, amount);
	}

	public void removeOrderItemCompletely(EntityID menuItemID) {
		this.getOrderItemAggregate().removeElementCompletely(menuItemID);
	}
	
	public void addAllOrderItems(AccumulatingAggregateEntry<DishMenuItemData>[] es) {
		this.getOrderItemAggregate().addAll(es);
	}
	
	protected AccumulatingOrderItemAggregate getOrderItemAggregate() {
		return (AccumulatingOrderItemAggregate) this.getAttributeValue(OrderAttribute.ORDER_ITEMS);
	}
	
	public Boolean getIsCash() {
		return (Boolean) this.getAttributeValue(OrderAttribute.IS_CASH);
	}
	
	public Boolean getIsHere() {
		return (Boolean) this.getAttributeValue(OrderAttribute.IS_HERE);
	}
	
	public LocalDateTime getDate() {
		return (LocalDateTime) this.getAttributeValue(OrderAttribute.DATE);
	}

	public String getNote() {
		return (String) this.getAttributeValue(OrderAttribute.NOTE);
	}

	public Integer getTableNumber() {
		return (Integer) this.getAttributeValue(OrderAttribute.TABLE_NUMBER);
	}
	
	@Override
	public OrderAttribute[] getAllAttributeEnumValues() {
		return OrderAttribute.values();
	}
	
	@Override
	public String toString() {
		return this.getOrderItemAggregate().toString() + " - " + this.getTableNumber();
	}
}
