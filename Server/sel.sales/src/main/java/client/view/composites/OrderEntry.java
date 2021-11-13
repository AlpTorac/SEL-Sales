package client.view.composites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.dish.IDishMenuData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import view.repository.IIndexedLayout;
import view.repository.ILabel;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderEntry extends UIVBoxLayout implements PriceUpdateTarget<MenuItemEntry> {
	private static final boolean DEFAULT_IS_CASH = false;
	private static final boolean DEFAULT_IS_HERE = false;
	
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private PriceUpdateTarget<OrderEntry> notifyTarget;
	
	private IOrderData activeData;
	private IDishMenuData activeMenu;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	private IIndexedLayout bottomPart;
	private ILabel priceDisplay;
	
	private String priceDisplayAffix = "Price: ";
	
	public OrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac, PriceUpdateTarget<OrderEntry> notifyTarget) {
		super(fac.createVBoxLayout().getComponent());
		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.notifyTarget = notifyTarget;
		this.initComponent();
	}
	
	public OrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac, PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
		this(controller, fac, advFac, notifyTarget);
		this.activeData = data;
		this.displayData(this.activeData);
	}

	public void displayData(IOrderData data) {
		this.resetUserInput();
		this.initMenuItemEntries(this.activeData = data);
	}
	
	protected void initComponent() {
		this.addUIComponent(this.bottomPart = this.initBottomPart());
	}
	
	protected IIndexedLayout initBottomPart() {
		IIndexedLayout bottom = this.fac.createHBoxLayout();
		
		bottom.addUIComponent(this.priceDisplay = this.initPriceDisplay());
		
		return bottom;
	}

	protected ILabel initPriceDisplay() {
		ILabel lbl = this.fac.createLabel();
		lbl.setCaption(this.priceDisplayAffix);
		return lbl;
	}

	protected void initMenuItemEntries(IOrderData data) {
		for (IOrderItemData itemData : data.getOrderedItems()) {
			this.addMenuItemEntry(itemData);
		}
	}
	
	protected void addMenuItemEntry(IOrderItemData data) {
		this.addMenuItemEntry(this.createItemEntry(data));
	}
	
	protected void addMenuItemEntry(MenuItemEntry entry) {
		this.menuItemEntries.add(entry);
		this.refreshMenu(this.activeMenu);
		this.addMenuItemEntryToUI(entry);
		this.refreshPriceDisplay();
	}
	
	protected void addMenuItemEntryToUI(MenuItemEntry entry) {
		this.addUIComponentBefore(entry, this.bottomPart);
	}
	
	public MenuItemEntry createItemEntry() {
		return new MenuItemEntry(controller, fac, advFac, this);
	}
	
	protected MenuItemEntry createItemEntry(IOrderItemData data) {
		MenuItemEntry e  = this.createItemEntry();
		e.displayData(data);
		return e;
	}
	
	@Override
	public void refreshPrice() {
		this.notifyTarget.refreshPrice();
		this.refreshPriceDisplay();
	}

	public void removeFromParent() {
		this.notifyTarget.remove(this);
		this.dettach();
	}
	
	@Override
	public void remove(MenuItemEntry referenceOfCaller) {
		this.menuItemEntries.remove(referenceOfCaller);
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		if (menuData != null) {
			this.activeMenu = menuData;
			this.menuItemEntries.forEach(mie -> mie.refreshMenu(this.activeMenu));
		}
	}
	
	protected void refreshPriceDisplay() {
		this.priceDisplay.setCaption(this.priceDisplayAffix + this.getNetPriceForDisplay());
	}
	
	protected String getNetPriceForDisplay() {
		return this.getNetPrice().toPlainString();
	}
	
	protected BigDecimal getNetPrice() {
		return this.menuItemEntries.stream().map(mie -> mie.getPrice()).reduce(BigDecimal.ZERO, (p1,p2)-> p1.add(p2));
	}
	
	public String getSerialisedOrderID() {
		return this.activeData.getID().toString();
	}
	
	protected IOrderItemData[] getCurrentOrder() {
		return this.menuItemEntries.stream()
		.map(mie -> this.controller.getModel().getOrderHelper().createOrderItemData(mie.getSelectedMenuItem(), mie.getAmount()))
		.toArray(IOrderItemData[]::new);
	}
	
	protected boolean isCash() {
		return DEFAULT_IS_CASH;
	}
	
	protected boolean isHere() {
		return DEFAULT_IS_HERE;
	}
	
	public String serialiseCurrentOrder() {
		return this.controller.getModel().getOrderHelper().serialiseForApp(
				this.getCurrentOrder(),
				LocalDateTime.now(),
				this.isCash(),
				this.isHere());
	}
	
	public void resetUserInput() {
		for (MenuItemEntry mie : this.menuItemEntries.toArray(MenuItemEntry[]::new)) {
			mie.removeFromParent();
		}
		this.refreshPrice();
	}
}
