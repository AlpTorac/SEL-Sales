package client.view.composites;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import controller.IController;
import model.id.EntityID;
import model.order.IOrderData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIAccordion;
import view.repository.uiwrapper.UIComponentFactory;

public abstract class OrderAccordion extends UIAccordion implements PriceUpdateTarget<OrderEntry> {
	
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private Map<EntityID, OrderEntry> orderEntries;
	
	public OrderAccordion(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createAccordion().getComponent());
		this.orderEntries = new ConcurrentHashMap<EntityID, OrderEntry>();
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
	}
	
	public void addOrderData(IOrderData data) {
		OrderEntry entry = this.createOrderEntry(data);
		EntityID id = data.getID();
		super.addTab(id.toString(), entry);
		OrderEntry pastEntry = this.orderEntries.put(id, entry);
		if (pastEntry != null) {
			this.remove(pastEntry);
		}
	}
	
	protected OrderEntry createOrderEntry(IOrderData data) {
		return new OrderEntry(controller, fac, this, data);
	}
	
	@Override
	public void refreshPrice() {
		
	}

	@Override
	public void remove(OrderEntry referenceOfCaller) {
		this.orderEntries.values().remove(referenceOfCaller);
	}
}
