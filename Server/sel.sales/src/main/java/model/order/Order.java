package model.order;

import java.math.BigDecimal;

import model.datamapper.OrderAttribute;
import model.dish.DishMenuItem;
import model.entity.Entity;
import model.entity.id.EntityID;

public class Order extends Entity<OrderAttribute> {
	private AccumulatingOrderItemAggregate orderItems;
	
	public Order(EntityID id) {
		super(id);
		this.orderItems = this.initOrderItemAggregate();
	}
	
	protected AccumulatingOrderItemAggregate initOrderItemAggregate() {
		return new AccumulatingOrderItemAggregate();
	}
	
	public void addOrderItem(DishMenuItem menuItem, BigDecimal amount) {
		this.getOrderItemAggregate().addElement(menuItem, amount);
	}
	
	public void removeOrderItem(EntityID menuItemID, BigDecimal amount) {
		this.getOrderItemAggregate().removeElement(menuItemID, amount);
	}

	public void removeOrderItemCompletely(EntityID menuItemID) {
		this.getOrderItemAggregate().removeElementCompletely(menuItemID);
	}
	
	protected AccumulatingOrderItemAggregate getOrderItemAggregate() {
		return this.orderItems;
	}
}
