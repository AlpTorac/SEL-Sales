package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IRadioButton;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class PendingPaymentOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	
	private PendingPaymentOrderAccordion accordion;
	
	protected PendingPaymentOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.initOrderAccordion();
	}

	protected void initOrderAccordion() {
		this.accordion = new PendingPaymentOrderAccordion(this.controller, this.fac);
		this.addUIComponent(this.accordion);
	}
	
	public void refreshDisplayedOrders(IOrderData[] datas) {
		this.accordion.removeAllTabs();
		for (IOrderData data : datas) {
			this.accordion.addOrderData(data);
		}
	}
	
	public PendingPaymentOrderAccordion getOrderAccordion() {
		return this.accordion;
	}
}
