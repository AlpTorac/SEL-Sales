package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemDataFactory;

public interface IOrderItem extends Comparable<IOrderItem> {
	default IOrderItemData getOrderItemData(IOrderItemDataFactory orderItemFac, IDishMenuItemDataFactory dishMenuItemFac) {
		return orderItemFac.orderItemToData(this, dishMenuItemFac);
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
