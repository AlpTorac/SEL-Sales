package client.view.composites;

import client.view.composites.listener.CancelOrderListener;
import client.view.composites.listener.EditOrderListener;
import client.view.composites.listener.SendOrderListener;
import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IHBoxLayout;
import view.repository.IIndexedLayout;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.Toggleable;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class PendingPaymentOrderEntry extends OrderEntry {
	private IVBoxLayout optionsArea;
	
	private IHBoxLayout paymentOptionLayout;
	private IToggleGroup isHere;
	private IRadioButton here;
	private IRadioButton toGo;
	
	private IHBoxLayout placeOptionLayout;
	private IToggleGroup isCash;
	private IRadioButton cash;
	private IRadioButton card;
	
	private IButton removeBtn;
	private IButton nextTabBtn;
	private IButton editBtn;

	public PendingPaymentOrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget,
			IOrderData data) {
		super(controller, fac, notifyTarget, data);
		
		this.optionsArea = this.getUIFactory().createVBoxLayout();
		this.optionsArea.addUIComponents(new IUIComponent[] {
				this.placeOptionLayout = this.initPlaceOptionsArea(),
				this.paymentOptionLayout = this.initPaymentOptionsArea()
		});
		
		this.addUIComponent(this.optionsArea);
	}
	
	protected IHBoxLayout initPlaceOptionsArea() {
		IHBoxLayout placeOptionsArea = this.getUIFactory().createHBoxLayout();
		this.isHere = this.getUIFactory().createToggleGroup();
		this.here = this.getUIFactory().createRadioButton();
		this.here.setCaption("Here");
		this.here.setToggled(true);
		this.toGo = this.getUIFactory().createRadioButton();
		this.toGo.setCaption("ToGo");
		this.isHere.addAllToToggleGroup(new Toggleable[] {this.here, this.toGo});
		placeOptionsArea.addUIComponent(this.here);
		placeOptionsArea.addUIComponent(this.toGo);
		return placeOptionsArea;
	}
	
	protected IHBoxLayout initPaymentOptionsArea() {
		IHBoxLayout paymentOptionsArea = this.getUIFactory().createHBoxLayout();
		this.isCash = this.getUIFactory().createToggleGroup();
		this.cash = this.getUIFactory().createRadioButton();
		this.cash.setCaption("Cash");
		this.cash.setToggled(true);
		this.card = this.getUIFactory().createRadioButton();
		this.card.setCaption("Card");
		this.isCash.addAllToToggleGroup(new Toggleable[] {this.cash, this.card});
		paymentOptionsArea.addUIComponent(this.cash);
		paymentOptionsArea.addUIComponent(this.card);
		return paymentOptionsArea;
	}
	
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = super.initBottomPart();
		bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
		bottom.addUIComponent(this.editBtn = this.initEditBtn());
		bottom.addUIComponent(this.removeBtn = this.initRemoveButton());
		return bottom;
	}
	
	protected IButton initRemoveButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Remove Order");
		btn.addClickListener(new CancelOrderListener(this, this.getController()));
		return btn;
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
	
	@Override
	protected void noEntryAction() {
		this.removeFromParent();
	}
}