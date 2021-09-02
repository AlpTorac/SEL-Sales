package model.order;

import java.math.BigDecimal;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public interface IOrderItemDataFactory {
	IOrderItemData constructData(IDishMenuItemData menuItem, BigDecimal amount);
	IOrderItemData orderItemToData(IOrderItem item);
}
