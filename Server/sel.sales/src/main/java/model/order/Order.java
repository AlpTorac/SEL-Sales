package model.order;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import model.dish.IDishMenuItemID;

public class Order implements IOrder {
	private Calendar date;
	private boolean cashOrCard;
	private boolean hereOrToGo;
	private IOrderID id;
	/**
	 * Order specific discount, FULLY INDEPENDENT of discounts of individual ordered items.
	 */
	private BigDecimal orderDiscount = BigDecimal.ZERO;
	
	private Map<IDishMenuItemID, IOrderItem> orderItems = new ConcurrentSkipListMap<IDishMenuItemID, IOrderItem>();
	
	Order(Calendar date, boolean cashOrCard, boolean hereOrToGo, IOrderID id) {
		this.date = date;
		this.cashOrCard = cashOrCard;
		this.hereOrToGo = hereOrToGo;
		this.id = id;
	}
	
	Order(IOrderData data) {
		this.date = data.getDate();
		this.cashOrCard = data.getCashOrCard();
		this.hereOrToGo = data.getHereOrToGo();
		this.id = data.getID();
	}
	
	/**
	 * @return True, if the item was new. False, if the id was already taken.
	 */
	@Override
	public boolean addOrderItem(IOrderItem item) {
		IDishMenuItemID id = item.getMenuItem().getID();
		if (!this.orderItems.containsKey(id)) {
			this.orderItems.put(id, item);
			return true;
		} else {
			IOrderItem formerOrder = this.orderItems.get(id);
			formerOrder.setAmount(formerOrder.getAmount().add(item.getAmount()));
			return false;
		}
	}

	@Override
	public boolean removeOrderItem(IDishMenuItemID id) {
		return this.orderItems.remove(id) != null;
	}

	@Override
	public IOrderItem getOrderItem(IDishMenuItemID id) {
		return this.orderItems.get(id);
	}

	@Override
	public IOrderItem[] getAllOrderItems() {
		return this.orderItems.values().toArray(IOrderItem[]::new);
	}

	@Override
	public boolean setOrderedItemAmount(IDishMenuItemID id, BigDecimal amount) {
		if (!this.orderItems.containsKey(id)) {
			return false;
		} else {
			IOrderItem formerOrder = this.orderItems.get(id);
			formerOrder.setAmount(amount);
			return true;
		}
	}

	@Override
	public IOrderID getID() {
		return this.id;
	}
	
	@Override
	public Calendar getDate() {
		return this.date;
	}
	
	@Override
	public boolean getCashOrCard() {
		return this.cashOrCard;
	}
	
	@Override
	public boolean getHereOrToGo() {
		return this.hereOrToGo;
	}

	@Override
	public Collection<IOrderItem> getOrderItemCollection() {
		return this.orderItems.values();
	}

	@Override
	public BigDecimal getOrderDiscount() {
		return this.orderDiscount;
	}

	@Override
	public void setOrderDiscount(BigDecimal orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
}
