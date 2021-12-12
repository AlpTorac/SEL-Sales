package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class PastOrdersArea extends OrderAreaTab {
	private IController controller;
	private UIComponentFactory fac;
	
	private PastOrderDisplay pendingSendDisplay;
	private PastOrderDisplay sentDisplay;
	private OrderEntryDisplay orderEntryDisplay;
	
	protected PastOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createHBoxLayout().getComponent());
		this.setSpacing(10);
		this.controller = controller;
		this.fac = fac;
		this.initPendingSendOrderAccordion();
		this.initSentOrderAccordion();
		this.initOrderEntryDisplay();
		this.getPendingSendOrderDisplay().addClickListener(new OrderEntryDisplayListener(this.orderEntryDisplay));
		this.getSentOrderDisplay().addClickListener(new OrderEntryDisplayListener(this.orderEntryDisplay));
	}

	protected void initSentOrderAccordion() {
		this.sentDisplay = new PastOrderDisplay(this.controller, this.fac);
		this.addUIComponent(this.sentDisplay.getComponent());
	}

	protected void initPendingSendOrderAccordion() {
		this.pendingSendDisplay = new PastOrderDisplay(this.controller, this.fac);
		this.addUIComponent(this.pendingSendDisplay.getComponent());
	}
	
	protected void initOrderEntryDisplay() {
		this.orderEntryDisplay = new OrderEntryDisplay(this.fac);
		this.addUIComponent(this.orderEntryDisplay.getComponent());
	}
	
	public void refreshPastOrdersTab(OrderData[] pendingSendOrders, OrderData[] sentOrders) {
		this.refreshPendingSendOrders(pendingSendOrders);
		this.refreshSentOrders(sentOrders);
	}
	
	protected void refreshPendingSendOrders(OrderData[] pendingSendOrders) {
		this.pendingSendDisplay.clear();
		for (OrderData data : pendingSendOrders) {
			this.pendingSendDisplay.addOrderData(data);
		}
	}
	
	protected void refreshSentOrders(OrderData[] sentOrders) {
		this.sentDisplay.clear();
		for (OrderData data : sentOrders) {
			this.sentDisplay.addOrderData(data);
		}
	}
	
	public PastOrderDisplay getPendingSendOrderDisplay() {
		return this.pendingSendDisplay;
	}
	
	public PastOrderDisplay getSentOrderDisplay() {
		return this.sentDisplay;
	}
}