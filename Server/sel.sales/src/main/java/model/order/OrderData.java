package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderData implements IOrderData {
	private Collection<IOrderItemData> orderItems;
	private LocalDateTime date;
	private boolean isCash;
	private boolean isHere;
	private String id;
	
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
				.anyMatch(item -> item.getGrossPrice().compareTo(BigDecimal.ZERO) < 0);
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public BigDecimal getGrossSum() {
//		BigDecimal result = BigDecimal.ZERO;
//		for (IOrderItemData d : this.orderItems) {
//			result = result.add(d.getGrossPrice());
//		}
//		return result;
		return this.orderItems.stream()
				.map(i -> i.getGrossPrice())
				.filter(gp -> gp.compareTo(BigDecimal.ZERO) > 0)
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
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
	public Collection<IOrderItemData> getOrderItems() {
		return new CopyOnWriteArrayList<IOrderItemData>(this.orderItems);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof IOrderData)) {
			return false;
		} else {
			IOrderData otherOrderData = (IOrderData) o;
			return this.getID().equals(otherOrderData.getID()) && 
					this.getIsCash() == otherOrderData.getIsCash() &&
					this.getIsHere() == this.getIsHere() && 
					this.getIsDiscounted() == this.getIsDiscounted() &&
					this.getDate().equals(otherOrderData.getDate()) && 
					this.getOrderItems().containsAll(otherOrderData.getOrderItems());
		}
	}

	@Override
	public BigDecimal getOrderDiscount() {
		return this.orderItems.stream()
				.map(i -> i.getGrossPrice())
				.filter(gp -> gp.compareTo(BigDecimal.ZERO) < 0)
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2))
				.abs();
	}

	@Override
	public IOrderData combine(IOrderData data) {
		return new OrderData(this.combineData(data), this.getDate(), this.getIsCash(), this.getIsHere(), this.getID());
	}
}
