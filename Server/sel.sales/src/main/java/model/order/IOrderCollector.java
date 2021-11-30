package model.order;

import model.order.datamapper.OrderAttribute;

public interface IOrderCollector {
	static final int TABLE_NUMBER_PLACEHOLDER = -1;
	
	void addOrder(IOrderData item);
	void removeOrder(String id);
	IOrderData getOrder(String id);
	IOrderData[] getAllOrders();
	void clearOrders();
	default int getPlaceholderTableNumber() {
		return TABLE_NUMBER_PLACEHOLDER;
	}
	boolean orderAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue);
	boolean contains(String orderID);
	IOrderData getOrderIfAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue);
	IOrderData[] getAllOrdersWithAttribute(OrderAttribute oa, Object attrValue);
	void removeAllOrdersWithAttribute(OrderAttribute oa, Object attrValue);
	void removeOrderIfAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue);
	void setOrderAttribute(String orderID, OrderAttribute oa, Object attrValue);
	Object getOrderAttribute(String orderID, OrderAttribute oa);
}
