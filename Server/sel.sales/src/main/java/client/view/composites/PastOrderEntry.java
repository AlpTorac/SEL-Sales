package client.view.composites;

import controller.IController;
import model.order.OrderData;
import view.repository.IHBoxLayout;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.Toggleable;
import view.repository.uiwrapper.UIComponentFactory;

public class PastOrderEntry extends OrderEntry {
	private IVBoxLayout optionsArea;
	
	private IHBoxLayout paymentOptionLayout;
	private IToggleGroup isHere;
	private IRadioButton here;
	private IRadioButton toGo;
	
	private IHBoxLayout placeOptionLayout;
	private IToggleGroup isCash;
	private IRadioButton cash;
	private IRadioButton card;
	
	public PastOrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget,
			OrderData data) {
		super(controller, fac, notifyTarget, data);
	}
	
	@Override
	public void displayData(OrderData data) {
		super.displayData(data);
		if (data != null) {
			this.getCashRadioButton().setToggled(data.getIsCash());
			this.getCardRadioButton().setToggled(!data.getIsCash());
			this.getHereRadioButton().setToggled(data.getIsHere());
			this.getToGoRadioButton().setToggled(!data.getIsHere());
		}
	}
	
	protected void addBottomPart() {
		super.addBottomPart();
		
		this.optionsArea = this.getUIFactory().createVBoxLayout();
		this.optionsArea.addUIComponents(new IUIComponent[] {
				this.placeOptionLayout = this.initPlaceOptionsArea(),
				this.paymentOptionLayout = this.initPaymentOptionsArea()
		});
		
		this.getHereRadioButton().setEnabled(false);
		this.getHereRadioButton().setOpacity(1);
		this.getToGoRadioButton().setEnabled(false);
		this.getToGoRadioButton().setOpacity(1);
		this.getCashRadioButton().setEnabled(false);
		this.getCashRadioButton().setOpacity(1);
		this.getCardRadioButton().setEnabled(false);
		this.getCardRadioButton().setOpacity(1);
		
		this.addComponentsVertically(this.optionsArea);
	}
	
	protected IHBoxLayout initPlaceOptionsArea() {
		IHBoxLayout placeOptionsArea = this.getUIFactory().createHBoxLayout();
		this.isHere = this.getUIFactory().createToggleGroup();
		this.here = this.getUIFactory().createRadioButton();
		this.getHereRadioButton().setCaption("Here");
		this.getHereRadioButton().setToggled(true);
		this.toGo = this.getUIFactory().createRadioButton();
		this.getToGoRadioButton().setCaption("ToGo");
		this.isHere.addAllToToggleGroup(new Toggleable[] {this.getHereRadioButton(), this.getToGoRadioButton()});
		placeOptionsArea.addUIComponent(this.getHereRadioButton());
		placeOptionsArea.addUIComponent(this.getToGoRadioButton());
		return placeOptionsArea;
	}
	
	protected IHBoxLayout initPaymentOptionsArea() {
		IHBoxLayout paymentOptionsArea = this.getUIFactory().createHBoxLayout();
		this.isCash = this.getUIFactory().createToggleGroup();
		this.cash = this.getUIFactory().createRadioButton();
		this.getCashRadioButton().setCaption("Cash");
		this.getCashRadioButton().setToggled(true);
		this.card = this.getUIFactory().createRadioButton();
		this.getCardRadioButton().setCaption("Card");
		this.isCash.addAllToToggleGroup(new Toggleable[] {this.getCashRadioButton(), this.getCardRadioButton()});
		paymentOptionsArea.addUIComponent(this.getCashRadioButton());
		paymentOptionsArea.addUIComponent(this.getCardRadioButton());
		return paymentOptionsArea;
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
	
	@Override
	protected boolean isCash() {
		return this.getCashRadioButton().isToggled();
	}
	
	@Override
	protected boolean isHere() {
		return this.getHereRadioButton().isToggled();
	}
	
	@Override
	protected void noEntryAction() {
		this.removeFromParent();
	}
}