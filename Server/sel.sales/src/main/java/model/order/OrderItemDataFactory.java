package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;

public class OrderItemDataFactory implements IOrderItemDataFactory {
	@Override
	public OrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount) {
		return new OrderItemData(menuItem, amount);
	}

	@Override
	public OrderItemData orderItemToData(IOrderItem item) {
		return new OrderItemData(item.getMenuItemData(), item.getAmount());
	}
}
