package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class CookingOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	
	private CookingOrderAccordion accordion;
	
	protected CookingOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.initOrderAccordion();
	}

	protected void initOrderAccordion() {
		this.accordion = new CookingOrderAccordion(this.controller, this.fac);
		this.addUIComponent(this.accordion);
	}
	
	public void refreshDisplayedOrders(OrderData[] datas) {
		this.accordion.removeAllTabs();
		for (OrderData data : datas) {
			this.accordion.addOrderData(data);
		}
	}
	
	public CookingOrderAccordion getOrderAccordion() {
		return this.accordion;
	}
}
