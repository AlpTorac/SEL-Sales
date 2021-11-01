package model.order.serialise;

import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;

public interface IOrderDeserialiser {
	IOrderData deserialise(String s);
	IOrderData[] deserialiseOrders(String s);
	void setFinder(IDishMenuItemFinder finder);
}
