package model.order;

public interface IOrderFactory {
	IOrder createOrder(IOrderData data);
}
