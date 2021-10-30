package model.order;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import model.id.EntityID;
import model.id.FixIDFactory;

public class OrderCollector implements IOrderCollector {
	private Map<EntityID, IOrder> orders = new ConcurrentSkipListMap<EntityID, IOrder>();
	private IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
	private IOrderDataFactory orderDataFac = new OrderDataFactory(this.orderItemDataFac, new FixIDFactory());
	private IOrderFactory orderFac = new OrderFactory();
	
	public OrderCollector() {
		
	}
	
	private IOrder getOrderFromMap(String id) {
		Optional<IOrder> optional = this.orders.entrySet().stream()
				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue()).findFirst();
		
		return optional.isPresent() ? optional.get() : null ;
	}
	
	@Override
	public boolean addOrder(IOrderData item) {
//		EntityID id = this.orderIDFac.createOrderID(item.getID());
		IOrder order = this.orderFac.createOrder(item);
		
		if (!this.orders.containsKey(item.getID())) {
			return this.orders.put(item.getID(), order) == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeOrder(String id) {
		return this.orders.keySet().removeIf(o -> o.serialisedIDequals(id));
	}

	@Override
	public IOrderData getOrder(String id) {
		IOrder order = this.getOrderFromMap(id);
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
