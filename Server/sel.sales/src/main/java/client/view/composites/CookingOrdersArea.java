package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class CookingOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private OrderAccordion accordion;
	
	protected CookingOrdersArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.initOrderAccordion();
	}

	protected void initOrderAccordion() {
		this.accordion = new OrderAccordion(this.controller, this.fac, this.advFac);
		this.addUIComponent(this.accordion);
	}
	
	public void refreshDisplayedOrders(IOrderData[] datas) {
		this.accordion.removeAllTabs();
		for (IOrderData data : datas) {
			this.accordion.addOrderData(data);
		}
	}
}
