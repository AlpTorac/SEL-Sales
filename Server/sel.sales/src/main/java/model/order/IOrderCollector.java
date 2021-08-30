package model.order;

public interface IOrderCollector {
	boolean addOrder(IOrder item);
	boolean removeOrder(IOrderID id);
	IOrder getOrder(IOrderID id);
	IOrder[] getAllOrders();
	void clearOrders();
}
