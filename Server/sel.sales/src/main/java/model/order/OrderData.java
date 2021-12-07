package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.entity.AccumulatingAggregateEntry;
import model.entity.ValueObject;
import model.entity.id.EntityID;

public class OrderData extends ValueObject<OrderAttribute> {
//	private AccumulatingOrderItemAggregate orderItems;
	
	protected OrderData(EntityID id) {
		super(id);
//		this.orderItems = this.initOrderItemAggregate();
	}
	
	public void setFinder(IDishMenuItemFinder finder) {
		this.getOrderItemAggregate().setDishMenuFinder(finder);
	}
	
	public void addOrderItem(DishMenuItemData menuItem, BigDecimal amount) {
		this.getOrderItemAggregate().addElement(menuItem, amount);
	}
	
	public void removeOrderItem(DishMenuItemData menuItem, BigDecimal amount) {
		this.getOrderItemAggregate().removeElement(menuItem, amount);
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
	
	public void addAllOrderItems(AccumulatingAggregateEntry<DishMenuItemData>[] es) {
		this.getOrderItemAggregate().addAll(es);
	}
	
	public BigDecimal getOrderedItemAmount(EntityID id) {
		return this.getOrderItemAggregate().getElementAmount(id);
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData>[] getOrderedItems() {
		return this.getOrderItemAggregate().getAllEntries();
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData> getOrderedItem(EntityID id) {
		return this.getOrderItemAggregate().getElementEntry(id);
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
	public String toString() {
		return this.getOrderItemAggregate().toString() + " - " + this.getTableNumber();
	}
	
	@Override
	public OrderAttribute[] getAllAttributeEnumValues() {
		return OrderAttribute.values();
	}
}
