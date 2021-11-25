package client.view.composites;

import client.view.composites.listener.AddCookingOrderListener;
import controller.IController;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IIndexedLayout;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class OrderTakingAreaOrderEntry extends OrderEntry {
	private IButton addMenuItemBtn;
	private IButton nextTabBtn;
	
	public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
			PriceUpdateTarget<OrderEntry> notifyTarget) {
		super(controller, fac, notifyTarget);
	}
	public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
			PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
		super(controller, fac, notifyTarget, data);
	}
	
	@Override
	public EditableMenuItemEntry getEntry(int pos) {
		return (EditableMenuItemEntry) super.getEntry(pos);
	}
	
	@Override
	public EditableMenuItemEntry createItemEntry() {
		return new EditableMenuItemEntry(this.getUIFactory(), this);
	}
	
	@Override
	protected void initComponent() {
		this.addUIComponent(this.addMenuItemBtn = this.initAddMenuItemButton());
		super.initComponent();
	}
	
	@Override
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = super.initBottomPart();
		bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
		return bottom;
	}
	
	protected IButton initAddMenuItemButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("+");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				addMenuItemEntry(createItemEntry());
				refreshPrice();
			}
		});
		return btn;
	}
	
	protected IButton initNextTabButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("To cooking area");
		
		ClickEventListener cel = new AddCookingOrderListener(this, this.getController());
		btn.addClickListener(cel);
		
		return btn;
	}
	
	@Override
	protected void addMenuItemEntryToUI(MenuItemEntry entry) {
		this.addUIComponentBefore(entry, this.addMenuItemBtn);
	}
	
	public IButton getAddMenuItemButton() {
		return this.addMenuItemBtn;
	}
	
	public IButton getNextTabButton() {
		return this.nextTabBtn;
	}
}