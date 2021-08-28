package model.order;

import java.math.BigDecimal;
import model.dish.IDishMenuItemData;

public interface IOrderItemDataFactory {
	IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount, BigDecimal discount);
	default IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount) {
		return this.constructData(menuItem, amount, BigDecimal.ZERO);
	}
	default IOrderItemData orderItemToData(IOrderItem item) {
		return item.getOrderItemData(this);
	}
}
