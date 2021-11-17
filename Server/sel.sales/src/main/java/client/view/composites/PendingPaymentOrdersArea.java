package client.view.composites;

import client.view.composites.listener.EditOrderListener;
import client.view.composites.listener.SendOrderListener;
import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IHBoxLayout;
import view.repository.IIndexedLayout;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.Toggleable;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class PendingPaymentOrdersArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private OrderAccordion accordion;
	
	protected PendingPaymentOrdersArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.initOrderAccordion();
	}

	protected void initOrderAccordion() {
		this.accordion = new PendingPaymentOrderAccordion(this.controller, this.fac, this.advFac);
		this.addUIComponent(this.accordion);
	}
	
	public void refreshDisplayedOrders(IOrderData[] datas) {
		this.accordion.removeAllTabs();
		for (IOrderData data : datas) {
			this.accordion.addOrderData(data);
		}
	}
	
	protected class PendingPaymentOrderAccordion extends OrderAccordion {

		public PendingPaymentOrderAccordion(IController controller, UIComponentFactory fac,
				AdvancedUIComponentFactory advFac) {
			super(controller, fac, advFac);
		}
		
		protected OrderEntry createOrderEntry(IOrderData data) {
			return new PendingPaymentOrderEntry(controller, fac, advFac, this, data);
		}
	}
	
	protected class PendingPaymentOrderEntry extends OrderEntry {
		private IHBoxLayout paymentOptionLayout;
		private IToggleGroup isHere;
		private IRadioButton here;
		private IRadioButton toGo;
		
		private IHBoxLayout placeOptionLayout;
		private IToggleGroup isCash;
		private IRadioButton cash;
		private IRadioButton card;
		
		private IButton nextTabBtn;
		private IButton editBtn;
		
		public PendingPaymentOrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac,
				PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
			super(controller, fac, notifyTarget, data);
			
			this.isHere = fac.createToggleGroup();
			this.here = fac.createRadioButton();
			this.here.setCaption("Here");
			this.here.setToggled(true);
			this.toGo = fac.createRadioButton();
			this.toGo.setCaption("ToGo");
			this.isHere.addAllToToggleGroup(new Toggleable[] {this.here, this.toGo});
			this.paymentOptionLayout = fac.createHBoxLayout();
			this.paymentOptionLayout.addUIComponent(this.here);
			this.paymentOptionLayout.addUIComponent(this.toGo);
			this.addUIComponent(this.paymentOptionLayout);
			
			this.isCash = fac.createToggleGroup();
			this.cash = fac.createRadioButton();
			this.cash.setCaption("Cash");
			this.cash.setToggled(true);
			this.card = fac.createRadioButton();
			this.card.setCaption("Card");
			this.isCash.addAllToToggleGroup(new Toggleable[] {this.cash, this.card});
			this.placeOptionLayout = fac.createHBoxLayout();
			this.placeOptionLayout.addUIComponent(this.cash);
			this.placeOptionLayout.addUIComponent(this.card);
			this.addUIComponent(this.placeOptionLayout);
		}
		
		protected IIndexedLayout initBottomPart() {
			IIndexedLayout bottom = super.initBottomPart();
			bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
			bottom.addUIComponent(this.editBtn = this.initEditBtn());
			return bottom;
		}
		
		protected IButton initNextTabButton() {
			IButton btn = fac.createButton();
			btn.setCaption("Send to server");
			
			ClickEventListener cel = new SendOrderListener(this, controller);
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
		
		protected boolean isCash() {
			return this.cash.isToggled();
		}
		
		protected boolean isHere() {
			return this.here.isToggled();
		}
	}
}
