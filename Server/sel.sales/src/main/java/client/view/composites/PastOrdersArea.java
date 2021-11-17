package client.view.composites;

import controller.IController;
import model.order.IOrderData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class PastOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private OrderAccordion pendingSendAccordion;
	private OrderAccordion sentAccordion;
	
	protected PastOrdersArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.setSpacing(50);
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.initPendingSendOrderAccordion();
		this.initSentOrderAccordion();
	}

	protected void initSentOrderAccordion() {
		this.sentAccordion = new PastOrderAccordion(this.controller, this.fac, this.advFac);
		this.addUIComponent(this.sentAccordion);
	}

	protected void initPendingSendOrderAccordion() {
		this.pendingSendAccordion = new PastOrderAccordion(this.controller, this.fac, this.advFac);
		this.addUIComponent(this.pendingSendAccordion);
	}
	
	public void refreshPastOrdersTab(IOrderData[] pendingSendOrders, IOrderData[] sentOrders) {
		this.refreshPendingSendOrders(pendingSendOrders);
		this.refreshSentOrders(sentOrders);
	}
	
	protected void refreshPendingSendOrders(IOrderData[] pendingSendOrders) {
		this.pendingSendAccordion.removeAllTabs();
		for (IOrderData data : pendingSendOrders) {
			this.pendingSendAccordion.addOrderData(data);
		}
	}
	
	protected void refreshSentOrders(IOrderData[] sentOrders) {
		this.sentAccordion.removeAllTabs();
		for (IOrderData data : sentOrders) {
			this.sentAccordion.addOrderData(data);
		}
	}
	
	protected class PastOrderAccordion extends OrderAccordion {
		public PastOrderAccordion(IController controller, UIComponentFactory fac,
				AdvancedUIComponentFactory advFac) {
			super(controller, fac, advFac);
		}
		
		protected OrderEntry createOrderEntry(IOrderData data) {
			return new PastOrderEntry(controller, fac, advFac, this, data);
		}
	}
	
	protected class PastOrderEntry extends OrderEntry {
		public PastOrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac,
				PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
			super(controller, fac, notifyTarget, data);
		}
	}
}