package model.order.serialise;

import model.order.IOrderData;

public interface IOrderDeserialiser {
	IOrderData deserialise(String s);
	IOrderData[] deserialiseOrders(String s);
}
