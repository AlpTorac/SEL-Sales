package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderData implements IOrderData {
	private Collection<IOrderItemData> orderItems;
	private LocalDateTime date;
	private boolean isCash;
	private boolean isHere;
	private String id;
	private BigDecimal orderDiscount = BigDecimal.ZERO;
	
	OrderData(Collection<IOrderItemData> orderItems, LocalDateTime date, boolean isCash, boolean isHere, String id) {
		this.orderItems = new CopyOnWriteArrayList<IOrderItemData>();
		this.orderItems.addAll(orderItems);
		this.date = date;
		this.isCash = isCash;
		this.isHere = isHere;
		this.id = id;
	}
	
	OrderData(IOrderItemData[] orderItems, LocalDateTime date, boolean isCash, boolean isHere, String id) {
		this.orderItems = new CopyOnWriteArrayList<IOrderItemData>();
		for (IOrderItemData d : orderItems) {
			this.orderItems.add(d);
		}
		this.date = date;
		this.isCash = isCash;
		this.isHere = isHere;
		this.id = id;
	}
	
	@Override
	public LocalDateTime getDate() {
		return this.date;
	}

	@Override
	public boolean getIsCash() {
		return this.isCash;
	}

	@Override
	public boolean getIsHere() {
		return this.isHere;
	}

	@Override
	public boolean getIsDiscounted() {
		return this.orderItems.stream()
				.anyMatch(item -> item.getTotalDiscount().compareTo(BigDecimal.ZERO) > 0);
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public BigDecimal getGrossSum() {
		BigDecimal result = BigDecimal.ZERO;
		for (IOrderItemData d : this.orderItems) {
			result = result.add(d.getNetPrice());
		}
		return result;
	}

	@Override
	public BigDecimal getTotalDiscount() {
		BigDecimal result = BigDecimal.ZERO;
		for (IOrderItemData d : this.orderItems) {
			result = result.add(d.getTotalDiscount());
		}
		return result.add(this.getOrderDiscount());
	}

	@Override
	public IOrderItemData[] getOrderedItems() {
		return this.orderItems.toArray(IOrderItemData[]::new);
	}
	
	@Override
	public String toString() {
		return this.getID().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IOrderData)) {
			return false;
		} else {
			IOrderData otherOrderData = (IOrderData) o;
			return this.getID().equals(otherOrderData.getID());
		}
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
