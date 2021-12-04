package model.order;

import model.datamapper.order.OrderAttribute;
import model.entity.IFactory;
import model.entity.Repository;

public class OrderCollector extends Repository<OrderAttribute, Order, OrderData> {
	@Override
	protected IFactory<OrderAttribute, Order, OrderData> getDefaultFactory() {
		return new OrderFactory();
	}
}
