package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemDataFactory;

public interface IOrderItem extends Comparable<IOrderItem> {
	default IOrderItemData getOrderItemData(IOrderItemDataFactory orderItemFac, IDishMenuItemDataFactory dishMenuItemFac) {
		return orderItemFac.orderItemToData(this, dishMenuItemFac);
	}
	default public BigDecimal getTotalPortions() {
		return this.getMenuItem().getPortionSize().multiply(this.getAmount());
	}
	default public BigDecimal getOrderItemPrice() {
		return this.getMenuItem().getPrice().multiply(this.getAmount());
	}
	IDishMenuItem getMenuItem();
	BigDecimal getAmount();
	default BigDecimal getDiscount() {
		return this.getMenuItem().getDiscount().multiply(this.getAmount());
	}
	void setAmount(BigDecimal amount);
	
	default public int compareTo(IOrderItem o) {
		return this.getMenuItem().compareTo(o.getMenuItem());
	}
}
