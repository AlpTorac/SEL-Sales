package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItem;

public interface IOrderItem extends Comparable<IOrderItem> {
	default IOrderItemData getOrderItemData(IOrderItemDataFactory fac) {
		return fac.orderItemToData(this);
	}
	IDishMenuItem getMenuItem();
	BigDecimal getAmount();
	BigDecimal getTotalPortions();
	BigDecimal getDiscount();
	void setAmount(BigDecimal amount);
	void setDiscount(BigDecimal discount);
	
	BigDecimal getOrderItemPrice();
	
	default public int compareTo(IOrderItem o) {
		return this.getMenuItem().compareTo(o.getMenuItem());
	}
}
