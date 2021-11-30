package model.order;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import model.id.EntityID;
import model.id.FixIDFactory;
import model.order.datamapper.OrderAttribute;

public class OrderCollector implements IOrderCollector {
	private Map<EntityID, OrderCollectorEntry> orders = new ConcurrentSkipListMap<EntityID, OrderCollectorEntry>();
	private IOrderItemDataFactory orderItemDataFac = new OrderItemDataFactory();
	private IOrderDataFactory orderDataFac = new OrderDataFactory(this.orderItemDataFac, new FixIDFactory());
	private IOrderFactory orderFac = new OrderFactory();
	
	public OrderCollector() {
		
	}
	
	protected IOrderData constructOrderData(IOrder order) {
		return this.orderDataFac.orderToData(order);
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
		OrderCollectorEntry oldEntry = this.getEntry(item.getID().toString());
		OrderCollectorEntry newEntry = new OrderCollectorEntry(this.orderFac.createOrder(item));
		
		if (oldEntry != null) {
			newEntry.setAllAttributesSameAs(oldEntry);
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
			return this.constructOrderData(order);
		}
		return null;
	}

	@Override
	public IOrderData[] getAllOrders() {
		return this.orders.values().stream()
				.map(i -> this.constructOrderData(i.getOrder()))
				.toArray(IOrderData[]::new);
	}

	@Override
	public void clearOrders() {
		this.orders.clear();
	}
	
	@Override
	public IOrderData getOrderIfAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue) {
		OrderCollectorEntry entry = this.getEntry(orderID);
		if (entry != null) {
			return entry.getAttribute(oa).equals(attributeValue) ? this.constructOrderData(entry.getOrder()) : null;
		}
		return null;
	}
	
	@Override
	public IOrderData[] getAllOrdersWithAttribute(OrderAttribute oa, Object attrValue) {
		return this.orders.values().stream().filter(oce -> oce.getAttribute(oa).equals(attrValue))
				.map(oce -> constructOrderData(oce.getOrder()))
				.toArray(IOrderData[]::new);
	}
	
	@Override
	public void removeAllOrdersWithAttribute(OrderAttribute oa, Object attrValue) {
		this.orders.values().removeIf(oce -> oce.getAttribute(oa).equals(attrValue));
	}
	
	@Override
	public void setOrderAttribute(String orderID, OrderAttribute oa, Object attrValue) {
		OrderCollectorEntry e = this.getEntry(orderID);
		if (e != null) {
			e.setAttribute(oa, attrValue);
		}
	}
	
	@Override
	public boolean contains(String orderID) {
		return this.getEntry(orderID) != null;
	}
	
	@Override
	public Object getOrderAttribute(String orderID, OrderAttribute oa) {
		OrderCollectorEntry e = this.getEntry(orderID);
		if (e != null) {
			return e.getAttribute(oa);
		}
		return null;
	}
	
	@Override
	public boolean orderAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue) {
		OrderCollectorEntry e = this.getEntry(orderID);
		if (e != null) {
			return e.getAttribute(oa).equals(attributeValue);
		}
		return false;
	}
	
	@Override
	public void removeOrderIfAttributeEquals(String orderID, OrderAttribute oa, Object attributeValue) {
		OrderCollectorEntry e = this.getEntry(orderID);
		if (e != null && e.getAttribute(oa).equals(attributeValue)) {
			this.orders.values().remove(e);
		}
	}
	
	protected class OrderCollectorEntry {
		private IOrder order;
		
		private Map<OrderAttribute, Object> attributes;
		
		protected OrderCollectorEntry(IOrder order) {
			this.order = order;
			this.initMap();
		}
		
		public void initMap() {
			this.attributes = new ConcurrentHashMap<OrderAttribute, Object>();
			
			this.attributes.put(OrderAttribute.STATUS, OrderStatus.UNDEFINED);
			this.attributes.put(OrderAttribute.IS_WRITTEN, false);
			this.attributes.put(OrderAttribute.TABLE_NUMBER, getPlaceholderTableNumber());
			this.attributes.put(OrderAttribute.NOTE, "");
		}
		
		public IOrder getOrder() {
			return this.order;
		}
		
		public Object getAttribute(OrderAttribute oa) {
			return this.attributes.get(oa);
		}
		
		public void setAttribute(OrderAttribute oa, Object value) {
			if (value != null) {
				this.attributes.put(oa, value);
			}
		}
		
		public void setAllAttributesSameAs(OrderCollectorEntry oce) {
			this.attributes.putAll(oce.getAttributeCollector());
		}
		
		private Map<OrderAttribute, Object> getAttributeCollector() {
			return this.attributes;
		}
	}
}
