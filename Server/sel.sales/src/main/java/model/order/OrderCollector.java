package model.order;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderCollector implements IOrderCollector {

	private Map<IOrderID, IOrder> orders = new ConcurrentSkipListMap<IOrderID, IOrder>();
	
	public OrderCollector() {
		
	}
	
	@Override
	public boolean addOrder(IOrder item) {
		IOrderID id = item.getID();
		
		if (!this.orders.containsKey(id)) {
			return this.orders.put(id, item) == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeOrder(IOrderID id) {
		return this.orders.remove(id) != null;
	}

	@Override
	public IOrder getOrderItem(IOrderID id) {
		return this.orders.get(id);
	}

	@Override
	public IOrder[] getAllOrders() {
		return this.orders.values().toArray(IOrder[]::new);
	}

}
