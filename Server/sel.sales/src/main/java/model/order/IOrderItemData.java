package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public interface IOrderItemData {
	IDishMenuItemData getItemData();
	BigDecimal getAmount();
	BigDecimal getPortionSize();
	BigDecimal getPricePerPortion();
	BigDecimal getTotalPrice();
	BigDecimal getDiscount();
}
