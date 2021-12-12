package client.view.composites;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIListView;

public abstract class OrderList extends UIListView<OrderEntry> implements PriceUpdateTarget<OrderEntry> {
	
	private IController controller;
	private UIComponentFactory fac;
	
//	private Map<EntityID, OrderEntry> orderEntries;
	
	public OrderList(IController controller, UIComponentFactory fac) {
		super(fac.createListView());
//		super.setPrefWidth(400);
//		this.orderEntries = new ConcurrentHashMap<EntityID, OrderEntry>();
		this.controller = controller;
		this.fac = fac;
	}
	
	public void addOrderData(OrderData data) {
		OrderEntry entry = this.createOrderEntry(data);
//		EntityID id = data.getID();
		super.addItemIfNotPresent(entry);
//		super.addTab(data.toString(), entry);
//		OrderEntry pastEntry = this.orderEntries.put(id, entry);
//		if (pastEntry != null) {
//			this.remove(pastEntry);
//		}
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
//		this.orderEntries.values().remove(referenceOfCaller);
//		OrderData data = referenceOfCaller.getActiveData();
		if (referenceOfCaller != null) {
			super.removeItem(referenceOfCaller);
		}
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	public Collection<OrderEntry> cloneOrderEntries() {
		Collection<OrderEntry> col = new CopyOnWriteArrayList<OrderEntry>();
//		this.orderEntries.values().forEach(e -> col.add(e.clone()));
		super.getAllItems().forEach(e -> col.add(e.clone()));
		return col;
	}
	
	public Collection<OrderData> getDisplayedOrders() {
		Collection<OrderData> col = new CopyOnWriteArrayList<OrderData>();
//		this.orderEntries.values().forEach(e -> col.add(e.getActiveData()));
		super.getAllItems().forEach(e -> col.add(e.getActiveData()));
		return col;
	}
	
	public OrderEntry getEntry(String orderID) {
		if (orderID != null) {
//			Optional<OrderEntry> o = this.orderEntries.values().stream()
//					.filter(e -> e.getSerialisedOrderID().equals(orderID))
//					.findFirst();
			Optional<OrderEntry> o = super.getAllItems().stream()
					.filter(e -> e.getSerialisedOrderID().equals(orderID))
					.findFirst();
			
			return o.isPresent() ? o.get() : null;
		}
		return null;
	}
	
	protected Collection<OrderEntry> getEntryCollection() {
//		return this.orderEntries.values();
		return super.getAllItems();
	}
}
