package model.order;

public interface IOrderCollector {
	boolean addOrder(IOrder item);
	boolean removeOrder(IOrderID id);
	IOrder getOrderItem(IOrderID id);
	IOrder[] getAllOrders();
}
