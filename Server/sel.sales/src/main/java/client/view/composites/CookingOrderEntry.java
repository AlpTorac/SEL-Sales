package client.view.composites;

import client.view.composites.listener.AddPendingPaymentOrderListener;
import client.view.composites.listener.CancelOrderListener;
import client.view.composites.listener.EditOrderListener;
import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IIndexedLayout;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class CookingOrderEntry extends OrderEntry {
	private IButton removeBtn;
	private IButton nextTabBtn;
	private IButton editBtn;
	
	public CookingOrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget,
			IOrderData data) {
		super(controller, fac, notifyTarget, data);
	}
	
	protected IButton initRemoveButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Remove Order");
		btn.addClickListener(new CancelOrderListener(this, this.getController()));
		return btn;
	}
	
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = super.initBottomPart();
		bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
		bottom.addUIComponent(this.editBtn = this.initEditBtn());
		bottom.addUIComponent(this.removeBtn = this.initRemoveButton());
		return bottom;
	}
	
	protected IButton initNextTabButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("To pending payment area");
		
		ClickEventListener cel = new AddPendingPaymentOrderListener(this, this.getController());
		btn.addClickListener(cel);
		
		return btn;
	}
	
	protected IButton initEditBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Edit order");
		
		ClickEventListener cel = new EditOrderListener(this, this.getController());
		btn.addClickListener(cel);
		
		return btn;
	}
	
	public IButton getNextTabButton() {
		return this.nextTabBtn;
	}
	
	public IButton getEditButton() {
		return this.editBtn;
	}
	
	@Override
	protected void noEntryAction() {
		this.removeFromParent();
	}
}
