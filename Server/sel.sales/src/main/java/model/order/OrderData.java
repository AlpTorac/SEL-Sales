package model.order;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

public class OrderData implements IOrderData {
	private Collection<IOrderItemData> orderItems;
	private Calendar date;
	private boolean cashOrCard;
	private boolean hereOrToGo;
	private IOrderID id;
	
	OrderData(Collection<IOrderItemData> orderItems, Calendar date, boolean cashOrCard, boolean hereOrToGo, IOrderID id) {
		this.orderItems = orderItems;
		this.date = date;
		this.cashOrCard = cashOrCard;
		this.hereOrToGo = hereOrToGo;
		this.id = id;
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
	public boolean getIsDiscounted() {
		return this.orderItems.stream()
				.anyMatch(item -> item.getDiscount().compareTo(BigDecimal.ZERO) > 0);
	}

	@Override
	public IOrderID getID() {
		return this.id;
	}

	@Override
	public BigDecimal getGrossSum() {
		BigDecimal result = BigDecimal.ZERO;
		this.orderItems.forEach((item) -> {result.add(item.getTotalPrice());});
		return result;
	}

	@Override
	public BigDecimal getTotalDiscount() {
		BigDecimal result = BigDecimal.ZERO;
		this.orderItems.forEach((item) -> {result.add(item.getDiscount());});
		return result;
	}

	@Override
	public IOrderItemData[] getOrderedItems() {
		return this.orderItems.toArray(IOrderItemData[]::new);
	}

}
