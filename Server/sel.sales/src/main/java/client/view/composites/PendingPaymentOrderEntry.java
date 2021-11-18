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
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class PendingPaymentOrderEntry extends OrderEntry {
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

	public PendingPaymentOrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget,
			IOrderData data) {
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
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Send to server");
		
		ClickEventListener cel = new SendOrderListener(this, this.getController());
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
	
	protected boolean isCash() {
		return this.cash.isToggled();
	}
	
	protected boolean isHere() {
		return this.here.isToggled();
	}
	
	public IRadioButton getHereRadioButton() {
		return here;
	}

	public IRadioButton getToGoRadioButton() {
		return toGo;
	}

	public IRadioButton getCashRadioButton() {
		return cash;
	}

	public IRadioButton getCardRadioButton() {
		return card;
	}

	public IButton getNextTabButton() {
		return nextTabBtn;
	}

	public IButton getEditButton() {
		return editBtn;
	}
}