package client.view.composites;

import client.view.composites.listener.AddPendingPaymentOrderListener;
import client.view.composites.listener.EditOrderListener;
import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IIndexedLayout;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
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
		this.accordion = new CookingOrderAccordion(this.controller, this.fac, this.advFac);
		this.addUIComponent(this.accordion);
	}
	
	public void refreshDisplayedOrders(IOrderData[] datas) {
		this.accordion.removeAllTabs();
		for (IOrderData data : datas) {
			this.accordion.addOrderData(data);
		}
	}
	
	protected class CookingOrderAccordion extends OrderAccordion {

		public CookingOrderAccordion(IController controller, UIComponentFactory fac,
				AdvancedUIComponentFactory advFac) {
			super(controller, fac, advFac);
		}
		
		protected OrderEntry createOrderEntry(IOrderData data) {
			return new CookingOrderEntry(controller, fac, advFac, this, data);
		}
	}
	
	protected class CookingOrderEntry extends OrderEntry {
		private IButton nextTabBtn;
		private IButton editBtn;
		
		public CookingOrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac,
				PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
			super(controller, fac, notifyTarget, data);
		}
		
		protected IIndexedLayout initBottomPart() {
			IIndexedLayout bottom = super.initBottomPart();
			bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
			bottom.addUIComponent(this.editBtn = this.initEditBtn());
			return bottom;
		}
		
		protected IButton initNextTabButton() {
			IButton btn = fac.createButton();
			btn.setCaption("To pending payment area");
			
			ClickEventListener cel = new AddPendingPaymentOrderListener(this, controller);
			btn.addClickListener(cel);
			
			return btn;
		}
		
		protected IButton initEditBtn() {
			IButton btn = fac.createButton();
			btn.setCaption("Edit order");
			
			ClickEventListener cel = new EditOrderListener(this, controller);
			btn.addClickListener(cel);
			
			return btn;
		}
	}
}
