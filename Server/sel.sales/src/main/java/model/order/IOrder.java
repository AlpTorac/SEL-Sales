package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public interface IOrder extends Comparable<IOrder> {
	String getID();
	boolean addOrderItem(IOrderItemData item);
	boolean removeOrderItem(String id);
	boolean setOrderedItemAmount(String id, BigDecimal amount);
	IOrderItem getOrderItem(String id);
	LocalDateTime getDate();
	boolean getCashOrCard();
	boolean getHereOrToGo();
	IOrderItem[] getAllOrderItems();
	Collection<IOrderItem> getOrderItemCollection();
	BigDecimal getOrderDiscount();
	void setOrderDiscount(BigDecimal orderDiscount);
	default public int compareTo(IOrder o) {
		return this.getID().compareTo(o.getID());
	}
}
