package model.order;

import java.math.BigDecimal;
import java.util.Calendar;

import model.dish.IDishMenuItemID;

public interface IOrder extends Comparable<IOrder> {
	default IOrderData getOrderData(IOrderDataFactory fac) {
		return fac.orderToData(this);
	}
	IOrderID getID();
	boolean addOrderItem(IOrderItem item);
	boolean removeOrderItem(IDishMenuItemID id);
	boolean setOrderedItemAmount(IDishMenuItemID id, BigDecimal amount);
	IOrderItem getOrderItem(IDishMenuItemID id);
	Calendar getDate();
	boolean getCashOrCard();
	boolean getHereOrToGo();
	IOrderItem[] getAllOrderItems();
	default public int compareTo(IOrder o) {
		return this.getID().compareTo(o.getID());
	}
}
