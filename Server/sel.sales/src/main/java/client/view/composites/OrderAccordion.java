package client.view.composites;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.entity.id.EntityID;
import model.order.OrderData;
import view.repository.uiwrapper.UIAccordion;
import view.repository.uiwrapper.UIComponentFactory;

public abstract class OrderAccordion extends UIAccordion implements PriceUpdateTarget<OrderEntry> {
	
	private IController controller;
	private UIComponentFactory fac;
	
	private Map<EntityID, OrderEntry> orderEntries;
	
	public OrderAccordion(IController controller, UIComponentFactory fac) {
		super(fac.createAccordion().getComponent());
		this.orderEntries = new ConcurrentHashMap<EntityID, OrderEntry>();
		this.controller = controller;
		this.fac = fac;
	}
	
	public void addOrderData(OrderData data) {
		OrderEntry entry = this.createOrderEntry(data);
		EntityID id = data.getID();
		super.addTab(data.toString() + " - " + data.getTableNumber(), entry);
		OrderEntry pastEntry = this.orderEntries.put(id, entry);
		if (pastEntry != null) {
			this.remove(pastEntry);
		}
	}
	
	protected abstract OrderEntry createOrderEntry(OrderData data);
//	{
//		return new OrderEntry(controller, fac, this, data);
//	}
	
	@Override
	public void refreshPrice() {
		
	}

	@Override
	public void remove(OrderEntry referenceOfCaller) {
		this.orderEntries.values().remove(referenceOfCaller);
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	public Collection<OrderEntry> cloneOrderEntries() {
		Collection<OrderEntry> col = new CopyOnWriteArrayList<OrderEntry>();
		this.orderEntries.values().forEach(e -> col.add(e.clone()));
		return col;
	}
	
	public Collection<OrderData> getDisplayedOrders() {
		Collection<OrderData> col = new CopyOnWriteArrayList<OrderData>();
		this.orderEntries.values().forEach(e -> col.add(e.getActiveData()));
		return col;
	}
	
	public OrderEntry getEntry(String orderID) {
		if (orderID != null) {
			Optional<OrderEntry> o = this.orderEntries.values().stream()
					.filter(e -> e.getSerialisedOrderID().equals(orderID))
					.findFirst();
			
			return o.isPresent() ? o.get() : null;
		}
		return null;
	}
	
	protected Collection<OrderEntry> getEntriesFromMap() {
		return this.orderEntries.values();
	}
}
