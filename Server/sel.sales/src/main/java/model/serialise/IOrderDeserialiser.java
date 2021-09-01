package model.serialise;

import model.order.IOrder;

public interface IOrderDeserialiser {
	IOrder deserialise(String s);
}
