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
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderEntry extends UIVBoxLayout implements PriceUpdateTarget<MenuItemEntry>, Cloneable {
	private static final boolean DEFAULT_IS_CASH = false;
	private static final boolean DEFAULT_IS_HERE = false;
	
	private IController controller;
	private UIComponentFactory fac;
	private PriceUpdateTarget<OrderEntry> notifyTarget;
	
	private IOrderData activeData;
	private IDishMenuData activeMenu;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	private IIndexedLayout bottomPart;
	private ILabel priceDisplay;
	
	private String priceDisplayAffix = "Price: ";
	
	public OrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget) {
		super(fac.createVBoxLayout().getComponent());
		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		this.controller = controller;
		this.fac = fac;
		this.notifyTarget = notifyTarget;
		this.initComponent();
	}
	
	public OrderEntry(IController controller, UIComponentFactory fac, PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
		this(controller, fac, notifyTarget);
		this.activeData = data;
		this.displayData(this.activeData);
	}

	protected void setActiveMenu(IDishMenuData menu) {
		this.activeMenu = menu;
	}
	
	public IDishMenuData getActiveMenu() {
		return this.activeMenu;
	}
	
	public IOrderData getActiveData() {
		return this.activeData;
	}
	
	protected void setActiveData(IOrderData data) {
		this.activeData = data;
	}
	
	public void displayData(IOrderData data) {
		if (data != null) {
			this.resetUserInput();
			this.setActiveData(data);
			this.initMenuItemEntries(this.getActiveData());
		}
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
		this.refreshMenu(this.getActiveMenu());
		this.addMenuItemEntryToUI(entry);
		this.refreshPriceDisplay();
	}
	
	protected void addMenuItemEntryToUI(MenuItemEntry entry) {
		this.addUIComponentBefore(entry, this.bottomPart);
	}
	
	protected MenuItemEntry createItemEntry() {
		return new MenuItemEntry(fac, this);
	}
	
	protected MenuItemEntry createItemEntry(IOrderItemData data) {
		MenuItemEntry e  = this.createItemEntry();
		e.displayData(data);
		return e;
	}
	
	@Override
	public void refreshPrice() {
		this.refreshPriceDisplay();
		this.notifyTarget.refreshPrice();
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
			this.setActiveMenu(menuData);
			this.menuItemEntries.forEach(mie -> mie.refreshMenu(this.getActiveMenu()));
			this.getNotifyTarget().refreshPrice();
		}
	}
	
	protected void refreshPriceDisplay() {
		this.priceDisplay.setCaption(this.priceDisplayAffix + this.getNetPriceForDisplay());
	}
	
	protected String getNetPriceForDisplay() {
		return this.getNetPrice().toPlainString();
	}
	
	public BigDecimal getNetPrice() {
		return this.menuItemEntries.stream().map(mie -> mie.getPrice()).reduce(BigDecimal.ZERO, (p1,p2)-> p1.add(p2));
	}
	
	public String getSerialisedOrderID() {
		return this.getActiveData().getID().toString();
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
	
	public Collection<MenuItemEntry> cloneMenuItemEntries() {
		Collection<MenuItemEntry> col = new CopyOnWriteArrayList<MenuItemEntry>();
		this.menuItemEntries.forEach(mie -> {col.add(mie.clone());});
		return col;
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	protected PriceUpdateTarget<OrderEntry> getNotifyTarget() {
		return this.notifyTarget;
	}
	
	protected OrderEntry constructClone() {
		return new OrderEntry(this.getController(), this.getUIFactory(), this.getNotifyTarget());
	}
	
	public OrderEntry clone() {
		OrderEntry clone = this.constructClone();
		clone.refreshMenu(this.getActiveMenu());
		clone.displayData(this.getActiveData());
		return clone;
	}
}
