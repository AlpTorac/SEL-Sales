package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public interface IOrderItem extends Comparable<IOrderItem> {
	default public BigDecimal getTotalPortions() {
		return this.getMenuItemData().getPortionSize().multiply(this.getAmount());
	}
	default public BigDecimal getOrderItemPrice() {
		return this.getMenuItemData().getGrossPrice().multiply(this.getAmount());
	}
	IDishMenuItemData getMenuItemData();
	BigDecimal getAmount();
	default BigDecimal getDiscount() {
		return this.getMenuItemData().getDiscount().multiply(this.getAmount());
	}
	void setAmount(BigDecimal amount);
	
	default public int compareTo(IOrderItem o) {
		return this.getMenuItemData().compareTo(o.getMenuItemData());
	}
}
