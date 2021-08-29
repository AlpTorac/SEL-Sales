package model.order;

import java.math.BigDecimal;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public interface IOrderItemDataFactory {
	IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount, BigDecimal discount);
	default IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount) {
		return this.constructData(menuItem, amount, BigDecimal.ZERO);
	}
	IOrderItemData orderItemToData(IOrderItem item, IDishMenuItemDataFactory fac);
}
