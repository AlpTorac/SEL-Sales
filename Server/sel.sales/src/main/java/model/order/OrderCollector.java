package model.order;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import model.id.EntityID;
import model.id.FixIDFactory;

public class OrderCollector implements IOrderCollector {
	private Map<EntityID, OrderCollectorEntry> orders = new ConcurrentSkipListMap<EntityID, OrderCollectorEntry>();
	private IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
	private IOrderDataFactory orderDataFac = new OrderDataFactory(this.orderItemDataFac, new FixIDFactory());
	private IOrderFactory orderFac = new OrderFactory();
	
	public OrderCollector() {
		
	}
	
	private OrderCollectorEntry getEntry(String id) {
		Optional<OrderCollectorEntry> optional = this.orders.entrySet().stream()
				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue()).findFirst();
		
		return optional.isPresent() ? optional.get() : null;
	}
	
	private IOrder getOrderFromMap(String id) {
		Optional<IOrder> optional = this.orders.entrySet().stream()
				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue().getOrder()).findFirst();
		
		return optional.isPresent() ? optional.get() : null;
	}
	
	@Override
	public void addOrder(IOrderData item) {
//		EntityID id = this.orderIDFac.createOrderID(item.getID());
//		IOrder order = this.orderFac.createOrder(item);
//		
//		if (!this.orders.containsKey(item.getID())) {
//			return this.orders.put(item.getID(), new OrderCollectorEntry(order)) == null;
//		} else {
//			return false;
//		}
		OrderCollectorEntry oldEntry = this.getEntry(item.getID().toString());
		OrderCollectorEntry newEntry = new OrderCollectorEntry(this.orderFac.createOrder(item));
		
		if (oldEntry != null) {
			newEntry.setOrderStatus(oldEntry.getOrderStatus());
			newEntry.setWritten(oldEntry.isWritten());
		}
		
		this.orders.put(item.getID(), newEntry);
	}

	@Override
	public void removeOrder(String id) {
		this.orders.keySet().removeIf(o -> o.serialisedIDequals(id));
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
		return this.orders.values().stream()
				.map(i -> this.orderDataFac.orderToData(i.getOrder()))
				.toArray(IOrderData[]::new);
	}

	@Override
	public void clearOrders() {
		this.orders.clear();
	}
	
	@Override
	public IOrderData[] getAllOrdersWithStatus(OrderStatus os) {
		return this.orders.values().stream()
				.filter(e -> e.getOrderStatus().equals(os))
				.map(i -> this.orderDataFac.orderToData(i.getOrder()))
				.toArray(IOrderData[]::new);
	}

	@Override
	public void editOrderStatus(String id, OrderStatus os) {
		this.orders.values().stream()
		.filter(e -> e.getOrder().getID().serialisedIDequals(id))
		.forEach(e -> e.setOrderStatus(os));
	}
	
	@Override
	public void removeOrdersWithStatus(OrderStatus os) {
		this.orders.values().removeIf(e -> e.getOrderStatus().equals(os));
	}
	
	@Override
	public boolean orderStatusEquals(String id, OrderStatus os) {
		return this.orders.values().stream()
				.filter(e -> e.getOrder().getID().toString().equals(id))
				.map(e -> e.getOrderStatus().equals(os))
				.reduce(false, (e1,e2) -> Boolean.logicalOr(e1, e2));
	}
	
	@Override
	public void editWritten(String id, boolean isWritten) {
		this.orders.values().stream()
		.filter(e -> e.getOrder().getID().toString().equals(id))
		.forEach(e -> e.setWritten(isWritten));
	}
	
	@Override
	public boolean isWritten(String id) {
		OrderCollectorEntry entry = this.getEntry(id);
		return entry != null ? entry.isWritten() : false;
	}
	
	@Override
	public IOrderData[] getAllWrittenOrders() {
		return this.orders.values().stream()
				.filter(e -> e.isWritten())
				.map(e -> this.orderDataFac.orderToData(e.getOrder()))
				.toArray(IOrderData[]::new);
	}
	
	protected class OrderCollectorEntry {
		private IOrder order;
		private OrderStatus os;
		private boolean isWritten;
		
		protected OrderCollectorEntry(IOrder order) {
			this.order = order;
			this.os = OrderStatus.UNDEFINED;
			this.isWritten = false;
		}
		
		public IOrder getOrder() {
			return this.order;
		}
		
		public OrderStatus getOrderStatus() {
			return this.os;
		}
		
		public void setOrderStatus(OrderStatus os) {
			this.os = os;
		}
		
		public boolean isWritten() {
			return this.isWritten;
		}
		
		public void setWritten(boolean isWritten) {
			this.isWritten = isWritten;
		}
	}
}
