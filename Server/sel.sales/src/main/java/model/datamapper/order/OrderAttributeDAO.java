package model.datamapper.order;

import model.datamapper.AttributeDAO;
import model.order.Order;
import model.order.OrderCollector;
import model.order.OrderData;

public abstract class OrderAttributeDAO extends AttributeDAO<OrderAttribute, Order, OrderData, OrderCollector> {

	protected OrderAttributeDAO(String fileAddress, String defaultFileName) {
		super(fileAddress, defaultFileName);
	}

}
