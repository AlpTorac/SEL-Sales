package model.order;

public interface IOrderCollector {
	boolean addOrder(IOrderData item);
	boolean removeOrder(String id);
	IOrderData getOrder(String id);
	IOrderData[] getAllOrders();
	void clearOrders();
}
