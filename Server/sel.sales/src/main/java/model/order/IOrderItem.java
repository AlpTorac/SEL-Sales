package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IOrderItem extends Comparable<IOrderItem> {
	default public BigDecimal getTotalPortions() {
		return this.getMenuItemData().getPortionSize().multiply(this.getAmount());
	}
	default public BigDecimal getOrderItemPrice() {
		return this.getMenuItemData().getGrossPrice().multiply(this.getAmount());
	}
	IDishMenuItemData getMenuItemData();
	BigDecimal getAmount();
	void setAmount(BigDecimal amount);
	
	default public int compareTo(IOrderItem o) {
		return this.getMenuItemData().compareTo(o.getMenuItemData());
	}
}
