package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import model.datamapper.OrderAttribute;
import model.entity.Aggregate;
import model.entity.ValueObject;
import model.entity.id.EntityID;

public class OrderData extends ValueObject<OrderAttribute> {
	private AccumulatingOrderItemAggregate orderItems;
	
	protected OrderData(EntityID id) {
		super(id);
		
		this.orderItems = new CopyOnWriteArrayList<AccumulatingOrderItemAggregate>(orderItems);
		this.flatten();
	}
	
	protected void setOrderItems() {
		
	}
	
	public boolean getIsDiscounted() {
		return this.orderItems.stream()
				.anyMatch(item -> item.getGrossPrice().compareTo(BigDecimal.ZERO) < 0);
	}
	
	public BigDecimal getGrossSum() {
		return this.orderItems.stream()
				.map(i -> i.getGrossPrice())
				.filter(gp -> gp.compareTo(BigDecimal.ZERO) > 0)
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
	}
	
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof OrderData)) {
			return false;
		} else {
			OrderData otherOrderData = (OrderData) o;
			return this.getID().equals(otherOrderData.getID()) && 
					this.getIsCash() == otherOrderData.getIsCash() &&
					this.getIsHere() == otherOrderData.getIsHere() && 
					this.getDate().equals(otherOrderData.getDate()) &&
					this.itemsEqual(otherOrderData);
		}
	}

	
	public BigDecimal getOrderDiscount() {
		BigDecimal result = BigDecimal.ZERO;
		for (AccumulatingOrderItemAggregate oi : this.getAllItems()) {
			if (oi.getAmount()) {
				
			}
		}
	}

	
	public OrderData combine(OrderData data) {
		return new OrderData(this.combineData(data), this.getDate(), this.getIsCash(), this.getIsHere());
	}

	
	public void flatten() {
		this.orderItems = this.flatten(this.orderItems);
	}
	
	public BigDecimal getNetSum() {
		return this.getGrossSum().subtract(this.getOrderDiscount());
	}
	
	public Collection<AccumulatingOrderItemAggregate> flatten(Collection<AccumulatingOrderItemAggregate> orderItems) {
		Map<EntityID, AccumulatingOrderItemAggregate> newOrderItems = new HashMap<EntityID, AccumulatingOrderItemAggregate>();
		orderItems.forEach(oid -> {
			if (oid.getAmount().compareTo(BigDecimal.ZERO) != 0) {
				EntityID id;
				if (newOrderItems.containsKey(id = oid.getMenuItem().getID())) {
					newOrderItems.put(id, newOrderItems.get(id).combine(oid));
				} else {
					newOrderItems.put(id, oid);
				}
			}
		});
		return newOrderItems.values();
	}
	
	public Collection<AccumulatingOrderItemAggregate> combineData(OrderData data) {
		Collection<AccumulatingOrderItemAggregate> orderItems = new ArrayList<AccumulatingOrderItemAggregate>();
		for (AccumulatingOrderItemAggregate oid : this.getAllItems()) {
			orderItems.add(oid);
		}
		for (AccumulatingOrderItemAggregate oid : data.getAllItems()) {
			orderItems.add(oid);
		}
		return this.flatten(orderItems);
	}
	
	public boolean itemsEqual(OrderData od) {
		return this.getOrderItems().stream()
				.map(oi -> {
					for (AccumulatingOrderItemAggregate oid : od.getOrderItems()) {
						if (oi.equals(oid)) {
							return true;
						}
					}
					return false;
				}).reduce(true, (b1,b2)->Boolean.logicalAnd(b1, b2)) && this.getOrderItems().size() == od.getOrderItems().size();
	}

	public void addOrderItem(AccumulatingOrderItemAggregate oi) {
		// TODO Auto-generated method stub
		
	}
	
	protected AccumulatingOrderItemAggregate getOrderItemAggregate() {
		return this.orderItems;
	}
}
