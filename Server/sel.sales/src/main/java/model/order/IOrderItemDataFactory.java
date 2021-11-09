package model.order;

import java.math.BigDecimal;
import model.dish.IDishMenuItemData;

public interface IOrderItemDataFactory {
	IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount);
	IOrderItemData orderItemToData(IOrderItem item);
}
