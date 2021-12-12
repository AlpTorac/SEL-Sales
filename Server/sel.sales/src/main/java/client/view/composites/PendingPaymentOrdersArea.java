package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class PendingPaymentOrdersArea extends OrderAreaTab {
	private PendingPaymentOrderDisplay orderDisplay;
	private OrderEntryDisplay orderEntryDisplay;
	
	protected PendingPaymentOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createHBoxLayout().getComponent());
		this.setSpacing(10);
		this.controller = controller;
		this.fac = fac;
		this.initOrderAccordion();
		this.initOrderEntryDisplay();
		this.getOrderDisplay().addClickListener(new OrderEntryDisplayListener(this.orderEntryDisplay));
	}

	protected void initOrderAccordion() {
		this.orderDisplay = new PendingPaymentOrderDisplay(this.controller, this.fac);
		this.addUIComponent(this.orderDisplay.getComponent());
	}
	
	protected void initOrderEntryDisplay() {
		this.orderEntryDisplay = new OrderEntryDisplay(this.fac);
		this.addUIComponent(this.orderEntryDisplay.getComponent());
	}
	
	public void refreshDisplayedOrders(OrderData[] datas) {
		this.orderDisplay.clear();
		for (OrderData data : datas) {
			this.orderDisplay.addOrderData(data);
		}
	}
	
	public PendingPaymentOrderDisplay getOrderDisplay() {
		return this.orderDisplay;
	}
}
