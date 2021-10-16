package model.order;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderCollector implements IOrderCollector {
	private Map<IOrderID, IOrder> orders = new ConcurrentSkipListMap<IOrderID, IOrder>();
	private IOrderIDFactory orderIDFac = new OrderIDFactory();
	private IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
	private IOrderDataFactory orderDataFac = new OrderDataFactory(this.orderItemDataFac);
	private IOrderFactory orderFac = new OrderFactory(this.orderIDFac);
	
	public OrderCollector() {
		
	}
	
	@Override
	public boolean addOrder(IOrderData item) {
		IOrderID id = this.orderIDFac.createOrderID(item.getID());
		IOrder order = this.orderFac.createOrder(item);
		
		if (!this.orders.containsKey(id)) {
			return this.orders.put(id, order) == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeOrder(String id) {
		return this.orders.remove(this.orderIDFac.createOrderID(id)) != null;
	}

	@Override
	public IOrderData getOrder(String id) {
		IOrder order = this.orders.get(this.orderIDFac.createOrderID(id));
		if (order != null) {
			return this.orderDataFac.orderToData(order);	
		}
		return null;
	}

	@Override
	public IOrderData[] getAllOrders() {
		return this.orders.values().stream().map(i -> this.orderDataFac.orderToData(i)).toArray(IOrderData[]::new);
	}

	@Override
	public void clearOrders() {
		this.orders.clear();
	}

}
