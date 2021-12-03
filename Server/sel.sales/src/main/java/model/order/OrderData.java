package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.entity.ValueObject;
import model.entity.id.EntityID;

public class OrderData extends ValueObject<OrderAttribute> {
//	private AccumulatingOrderItemAggregate orderItems;
	
	protected OrderData(EntityID id) {
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
	
	public boolean getIsDiscounted() {
		return this.getOrderItemAggregate().getIsDiscounted();
	}
	
	public BigDecimal getGrossSum() {
		return this.getOrderItemAggregate().getGrossSum();
	}
	
	public BigDecimal getOrderDiscount() {
		return this.getOrderItemAggregate().getOrderDiscount();
	}
	
	public BigDecimal getNetSum() {
		return this.getOrderItemAggregate().getNetSum();
	}
	
	protected AccumulatingOrderItemAggregate getOrderItemAggregate() {
		return (AccumulatingOrderItemAggregate) this.getAttributeValue(OrderAttribute.ORDER_ITEMS);
	}
	
	public void addAllOrderItems(Iterable<AccumulatingAggregateEntry<DishMenuItemData>> es) {
		this.getOrderItemAggregate().addAll(es);
	}
	
	@SuppressWarnings("unchecked")
	public void addAllOrderItems(AccumulatingAggregateEntry<DishMenuItemData>... es) {
		this.getOrderItemAggregate().addAll(es);
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData>[] getOrderedItems() {
		return this.getOrderItemAggregate().getAllEntries();
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
}
