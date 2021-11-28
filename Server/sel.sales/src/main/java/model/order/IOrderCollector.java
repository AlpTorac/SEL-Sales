package model.order;

public interface IOrderCollector {
	static final int TABLE_NUMBER_PLACEHOLDER = -1;
	
	default void addOrder(IOrderData item, OrderStatus os) {
		this.addOrder(item);
		this.editOrderStatus(item.getID().toString(), os);
	}
	OrderStatus getOrderStatus(String id);
	void addOrder(IOrderData item);
	void editOrderStatus(String id, OrderStatus os);
	void editWritten(String id, boolean isWritten);
	void removeOrder(String id);
	IOrderData getOrder(String id);
	default IOrderData getOrderIfStatusEqual(String id, OrderStatus os) {
		IOrderData data = this.getOrder(id);
		if (data != null && this.orderStatusEquals(id, os)) {
			return data;
		}
		return null;
	}
	default void removeOrderIfStatusEqual(String id, OrderStatus os) {
		IOrderData data = this.getOrder(id);
		if (data != null && this.orderStatusEquals(id, os)) {
			this.removeOrder(id);
		}
	}
	IOrderData[] getAllOrders();
	IOrderData[] getAllOrdersWithStatus(OrderStatus os);
	IOrderData[] getAllWrittenOrders();
	boolean orderStatusEquals(String id, OrderStatus os);
	boolean isWritten(String id);
	void removeOrdersWithStatus(OrderStatus os);
	void clearOrders();
	int getTableNumber(String orderID);
	void setTableNumber(String orderID, int tableNumber);
	default int getPlaceholderTableNumber() {
		return TABLE_NUMBER_PLACEHOLDER;
	}
	boolean contains(String orderID);
}
