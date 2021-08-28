package model.order;

import model.IDishMenuItemFinder;

public interface IOrderFactory {
	IOrder createOrder(IDishMenuItemFinder finder, IOrderData data);
}
