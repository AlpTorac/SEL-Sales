package model.order;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemID;

public interface IOrder extends Comparable<IOrder> {
	default IOrderData getOrderData(IOrderDataFactory orderDataFac, IOrderItemDataFactory orderItemDataFac, IDishMenuItemDataFactory dishMenuItemDataFac) {
		return orderDataFac.orderToData(this, orderItemDataFac, dishMenuItemDataFac);
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
	Collection<IOrderItem> getOrderItemCollection();
	default public int compareTo(IOrder o) {
		return this.getID().compareTo(o.getID());
	}
}
