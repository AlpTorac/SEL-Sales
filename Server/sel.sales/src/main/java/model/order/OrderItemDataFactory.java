package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public class OrderItemDataFactory implements IOrderItemDataFactory {
	@Override
	public OrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount, BigDecimal discount) {
		return new OrderItemData(menuItem, amount, discount);
	}
}
