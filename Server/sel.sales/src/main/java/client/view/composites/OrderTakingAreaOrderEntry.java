package client.view.composites;

import client.view.composites.listener.AddCookingOrderListener;
import client.view.composites.listener.CancelOrderListener;
import controller.IController;
import model.order.OrderData;
import view.repository.IButton;
import view.repository.IIndexedLayout;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class OrderTakingAreaOrderEntry extends OrderEntry {
	private IButton addMenuItemBtn;
	private IButton nextTabBtn;
	private IButton clearBtn;
	
	/**
	 * Contains the most recently passed orderID
	 */
	private String mostRecentOrderID;
	
	public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
			PriceUpdateTarget<OrderEntry> notifyTarget) {
		super(controller, fac, notifyTarget);
	}
	public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
			PriceUpdateTarget<OrderEntry> notifyTarget, OrderData data) {
		super(controller, fac, notifyTarget, data);
	}
	
	@Override
	protected void tableNumberChoiceBoxSetup() {

	}
	
	@Override
	protected void noteBoxSetupExtras() {
		
	}
	
	@Override
	public void orderSentToNextTab() {
//		this.getController().getModel().setOrderTableNumber(this.mostRecentOrderID, this.getTableNumberSelection());
//		this.getController().getModel().setOrderNote(this.mostRecentOrderID, this.getCurrentOrderNote());
		this.resetUserInput();
	}
	
	@Override
	public String getSerialisedOrderID() {
		String orderID;
		if (this.getActiveData() == null) {
			orderID = this.getController().getModel().getDateSettings().serialiseDateFromNow();
		} else {
			orderID = super.getSerialisedOrderID();
		}
		this.mostRecentOrderID = orderID;
		return this.mostRecentOrderID;
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
	protected void addBottomPart() {
		this.addComponentsVertically(this.addMenuItemBtn = this.initAddMenuItemButton());
		super.addBottomPart();
	}
	
	@Override
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = super.initBottomPart();
		bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
		bottom.addUIComponent(this.clearBtn = this.initClearButton());
		return bottom;
	}
	
	protected IButton initClearButton() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Clear");
		btn.addClickListener(new CancelOrderListener(this, this.getController()));
		return btn;
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
		this.getVBoxLayout().addUIComponentBefore(entry, this.addMenuItemBtn);
	}
	
	public IButton getAddMenuItemButton() {
		return this.addMenuItemBtn;
	}
	
	public IButton getNextTabButton() {
		return this.nextTabBtn;
	}
	
	public IButton getClearButton() {
		return this.clearBtn;
	}
	
	public void setTableNumber(Integer i) {
		this.getTableNumberChoiceBox().artificiallySelectItem(i);
	}
	
	public void setOrderNode(String orderNode) {
		this.getOrderNoteBox().setCaption(orderNode);
	}
}