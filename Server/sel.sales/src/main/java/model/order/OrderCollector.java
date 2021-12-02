package model.order;

import model.datamapper.OrderAttribute;
import model.entity.Repository;

public class OrderCollector extends Repository<OrderAttribute, Order> {
//	private OrderDataFactory orderDataFac = new OrderDataFactory(this.orderItemDataFac, new MinimalIDFactory());
//	private OrderFactory orderFac = new OrderFactory();
//	
//	public OrderCollector() {
//		
//	}
//	
//	protected IOrderData constructOrderData(IOrder order) {
//		return this.orderDataFac.orderToData(order);
//	}
//	
//	private OrderCollectorEntry getEntry(String id) {
//		Optional<OrderCollectorEntry> optional = this.orders.entrySet().stream()
//				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue()).findFirst();
//		
//		return optional.isPresent() ? optional.get() : null;
//	}
//	
//	private IOrder getOrderFromMap(String id) {
//		Optional<IOrder> optional = this.orders.entrySet().stream()
//				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue().getOrder()).findFirst();
//		
//		return optional.isPresent() ? optional.get() : null;
//	}
//	
//	@Override
//	public void addElement(IOrderData item) {
//		OrderCollectorEntry oldEntry = this.getEntry(item.getID().toString());
//		OrderCollectorEntry newEntry = new OrderCollectorEntry(this.orderFac.createOrder(item));
//		
//		if (oldEntry != null) {
//			newEntry.setAllAttributesSameAs(oldEntry);
//		}
//		
//		this.orders.put(item.getID(), newEntry);
//	}
//
//	@Override
//	public IOrderData removeElement(String id) {
//		this.orders.keySet().removeIf(o -> o.serialisedIDequals(id));
//	}
//
//	@Override
//	public IOrderData getElement(String id) {
//		IOrder order = this.getOrderFromMap(id);
//		if (order != null) {
//			return this.constructOrderData(order);
//		}
//		return null;
//	}
//
//	@Override
//	public IOrderData[] getAllElements() {
//		return this.orders.values().stream()
//				.map(i -> this.constructOrderData(i.getOrder()))
//				.toArray(IOrderData[]::new);
//	}
//
//	@Override
//	public void clearAllElements() {
//		this.orders.clear();
//	}
//	
//	@Override
//	public IOrderData getElementIfAttributeValueEquals(String orderID, IAttribute oa, Object attributeValue) {
//		OrderCollectorEntry entry = this.getEntry(orderID);
//		if (entry != null) {
//			return entry.getAttribute(oa).equals(attributeValue) ? this.constructOrderData(entry.getOrder()) : null;
//		}
//		return null;
//	}
//	
//	@Override
//	public IOrderData[] getAllElementsByAttributeValue(IAttribute oa, Object attrValue) {
//		return this.orders.values().stream().filter(oce -> oce.getAttribute(oa).equals(attrValue))
//				.map(oce -> constructOrderData(oce.getOrder()))
//				.toArray(IOrderData[]::new);
//	}
//	
//	@Override
//	public void removeAllElementsWithAttributeValue(IAttribute oa, Object attrValue) {
//		this.orders.values().removeIf(oce -> oce.getAttribute(oa).equals(attrValue));
//	}
//	
//	@Override
//	public void setAttributeValue(String orderID, OrderAttribute oa, Object attrValue) {
//		OrderCollectorEntry e = this.getEntry(orderID);
//		if (e != null) {
//			e.setAttribute(oa, attrValue);
//		}
//	}
//	
//	@Override
//	public boolean contains(String orderID) {
//		return this.getEntry(orderID) != null;
//	}
//	
//	@Override
//	public Object getAttributeValue(String orderID, IAttribute oa) {
//		OrderCollectorEntry e = this.getEntry(orderID);
//		if (e != null) {
//			return e.getAttribute(oa);
//		}
//		return null;
//	}
//	
//	@Override
//	public boolean attributeValueEquals(String orderID, IAttribute oa, Object attributeValue) {
//		OrderCollectorEntry e = this.getEntry(orderID);
//		if (e != null) {
//			return e.getAttribute(oa).equals(attributeValue);
//		}
//		return false;
//	}
//	
//	@Override
//	public void removeElementIfAttributeValueEquals(String orderID, IAttribute oa, Object attributeValue) {
//		OrderCollectorEntry e = this.getEntry(orderID);
//		if (e != null && e.getAttribute(oa).equals(attributeValue)) {
//			this.orders.values().remove(e);
//		}
//	}
//	
//	protected class OrderCollectorEntry {
//		private IOrder order;
//		
//		private Map<OrderAttribute, Object> attributes;
//		
//		protected OrderCollectorEntry(IOrder order) {
//			this.order = order;
//			this.initMap();
//		}
//		
//		public void initMap() {
//			this.attributes = new ConcurrentHashMap<OrderAttribute, Object>();
//			
//			this.attributes.put(OrderAttribute.STATUS, OrderStatus.UNDEFINED);
//			this.attributes.put(OrderAttribute.IS_WRITTEN, false);
//			this.attributes.put(OrderAttribute.TABLE_NUMBER, getPlaceholderTableNumber());
//			this.attributes.put(OrderAttribute.NOTE, "");
//		}
//		
//		public IOrder getOrder() {
//			return this.order;
//		}
//		
//		public Object getAttribute(IAttribute oa) {
//			return this.attributes.get(oa);
//		}
//		
//		public void setAttribute(OrderAttribute oa, Object value) {
//			if (value != null) {
//				this.attributes.put(oa, value);
//			}
//		}
//		
//		public void setAllAttributesSameAs(OrderCollectorEntry oce) {
//			this.attributes.putAll(oce.getAttributeCollector());
//		}
//		
//		private Map<OrderAttribute, Object> getAttributeCollector() {
//			return this.attributes;
//		}
//	}
}
