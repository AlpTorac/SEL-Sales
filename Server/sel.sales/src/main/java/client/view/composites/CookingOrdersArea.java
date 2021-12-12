package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.uiwrapper.UIComponentFactory;

public class CookingOrdersArea extends OrderAreaTab {
	private IController controller;
	private UIComponentFactory fac;
	
	private CookingOrderDisplay orderDisplay;
	private OrderEntryDisplay orderEntryDisplay;
	
	protected CookingOrdersArea(IController controller, UIComponentFactory fac) {
		super(fac.createHBoxLayout().getComponent());
		this.setSpacing(10);
		this.controller = controller;
		this.fac = fac;
		this.initOrderAccordion();
		this.initOrderEntryDisplay();
		this.getOrderDisplay().addClickListener(new OrderEntryDisplayListener(this.orderEntryDisplay));
	}

	protected void initOrderEntryDisplay() {
		this.orderEntryDisplay = new OrderEntryDisplay(this.fac);
		this.addUIComponent(this.orderEntryDisplay.getComponent());
	}
	
	protected void initOrderAccordion() {
		this.orderDisplay = new CookingOrderDisplay(this.controller, this.fac);
		this.addUIComponent(this.getOrderDisplay().getComponent());
	}
	
	public void refreshDisplayedOrders(OrderData[] datas) {
		this.getOrderDisplay().clear();
		for (OrderData data : datas) {
			this.getOrderDisplay().addOrderData(data);
		}
	}
	
	public CookingOrderDisplay getOrderDisplay() {
		return this.orderDisplay;
	}
}
