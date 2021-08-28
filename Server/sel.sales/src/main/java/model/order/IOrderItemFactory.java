package model.order;

import model.IDishMenuItemFinder;

public interface IOrderItemFactory {
	IOrderItem createOrderItem(IDishMenuItemFinder finder, IOrderItemData item);
}
