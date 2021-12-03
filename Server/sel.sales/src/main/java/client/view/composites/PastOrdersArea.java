package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class PastOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	
	private PastOrderAccordion pendingSendAccordion;
	private PastOrderAccordion sentAccordion;
	
	protected PastOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.setSpacing(50);
		this.controller = controller;
		this.fac = fac;
		this.initPendingSendOrderAccordion();
		this.initSentOrderAccordion();
	}

	protected void initSentOrderAccordion() {
		this.sentAccordion = new PastOrderAccordion(this.controller, this.fac);
		this.addUIComponent(this.sentAccordion);
	}

	protected void initPendingSendOrderAccordion() {
		this.pendingSendAccordion = new PastOrderAccordion(this.controller, this.fac);
		this.addUIComponent(this.pendingSendAccordion);
	}
	
	public void refreshPastOrdersTab(OrderData[] pendingSendOrders, OrderData[] sentOrders) {
		this.refreshPendingSendOrders(pendingSendOrders);
		this.refreshSentOrders(sentOrders);
	}
	
	protected void refreshPendingSendOrders(OrderData[] pendingSendOrders) {
		this.pendingSendAccordion.removeAllTabs();
		for (OrderData data : pendingSendOrders) {
			this.pendingSendAccordion.addOrderData(data);
		}
	}
	
	protected void refreshSentOrders(OrderData[] sentOrders) {
		this.sentAccordion.removeAllTabs();
		for (OrderData data : sentOrders) {
			this.sentAccordion.addOrderData(data);
		}
	}
	
	public PastOrderAccordion getPendingSendOrderAccordion() {
		return this.pendingSendAccordion;
	}
	
	public PastOrderAccordion getSentOrderAccordion() {
		return this.sentAccordion;
	}
}