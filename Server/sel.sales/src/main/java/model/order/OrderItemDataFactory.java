package model.order;

import java.math.BigDecimal;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public class OrderItemDataFactory implements IOrderItemDataFactory {
	@Override
	public OrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount, BigDecimal discount) {
		return new OrderItemData(menuItem, amount, discount);
	}

	@Override
	public OrderItemData orderItemToData(IOrderItem item, IDishMenuItemDataFactory fac) {
		return new OrderItemData(item.getMenuItem().getDishMenuItemData(fac),
				item.getAmount(),
				item.getDiscount());
	}
}
