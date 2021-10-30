package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import model.id.EntityID;

public interface IOrder extends Comparable<IOrder> {
	EntityID getID();
	boolean addOrderItem(IOrderItemData item);
	boolean removeOrderItem(String id);
	boolean setOrderedItemAmount(String id, BigDecimal amount);
	IOrderItem getOrderItem(String id);
	LocalDateTime getDate();
	boolean getIsCash();
	boolean getIsHere();
	IOrderItem[] getAllOrderItems();
	Collection<IOrderItem> getOrderItemCollection();
	default public int compareTo(IOrder o) {
		return this.getID().compareTo(o.getID());
	}
}
