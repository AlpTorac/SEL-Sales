package client.view.composites;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import client.view.composites.listener.AddCookingOrderListener;
import controller.IController;
import model.dish.IDishMenuData;
import model.order.IOrderItemData;
import view.repository.IButton;
import view.repository.ILabel;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTakingArea extends UIVBoxLayout implements PriceUpdateTarget<MenuItemEntry> {
	private static final boolean DEFAULT_IS_CASH = false;
	private static final boolean DEFAULT_IS_HERE = false;
	
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	private IButton addMenuItemBtn;
	private ILabel priceDisplay;
	private IButton nextTabBtn;
	
	private String priceDisplayAffix = "Price: ";
	
	private IDishMenuData activeMenu;
	
	protected OrderTakingArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.init();
	}

	protected void init() {
		this.addMenuItemBtn = this.fac.createButton();
		this.addMenuItemBtn.setCaption("+");
		this.addMenuItemBtn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				addMenuItemEntry();
			}
		});
		this.addUIComponent(this.addMenuItemBtn);
		
		UIHBoxLayout bottomPart = this.fac.createHBoxLayout();
		bottomPart.setSpacing(50);
		bottomPart.addUIComponents(new IUIComponent[] {
			this.priceDisplay = this.initPriceDisplay(),
					this.nextTabBtn = this.initNextTabButton()
		});
		
		this.addUIComponent(bottomPart);
		
		this.setPrefHeight(800);
	}
	
	protected IButton initNextTabButton() {
		IButton btn = this.fac.createButton();
		btn.setCaption("Add to cooking");
		
		ClickEventListener aol = new AddCookingOrderListener(this, this.controller);
		btn.addClickListener(aol);
		
		return btn;
	}

	protected ILabel initPriceDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption(this.priceDisplayAffix);
		return label;
	}

	protected void refreshPriceDisplay() {
		this.priceDisplay.setCaption(this.priceDisplayAffix + this.getNetPrice());
	}
	
	protected String getNetPriceForDisplay() {
		return this.getNetPrice().toPlainString();
	}
	
	protected BigDecimal getNetPrice() {
		return this.menuItemEntries.stream().map(mie -> mie.getPrice()).reduce(BigDecimal.ZERO, (p1,p2)-> p1.add(p2));
	}

	protected void addMenuItemEntry() {
		MenuItemEntry entry = this.createMenuItemEntry();
		entry.refreshMenu(this.activeMenu);
		this.menuItemEntries.add(entry);
		this.addUIComponentBefore(entry, this.addMenuItemBtn);
	}
	
	protected MenuItemEntry createMenuItemEntry() {
		return new EditableMenuItemEntry(this.controller, this.fac, this.advFac, this);
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.activeMenu = menuData;
		this.menuItemEntries.forEach(mie -> mie.refreshMenu(this.activeMenu));
	}

	@Override
	public void refreshPrice() {
		this.refreshPriceDisplay();
	}

	@Override
	public void remove(MenuItemEntry referenceOfCaller) {
		this.menuItemEntries.remove(referenceOfCaller);
	}
	
	public String serialiseCurrentOrder() {
		IOrderItemData[] data =  this.menuItemEntries.stream()
		.map(mie -> this.controller.getModel().getOrderHelper().createOrderItemData(mie.getSelectedMenuItem(), mie.getAmount()))
		.toArray(IOrderItemData[]::new);
		
		return this.controller.getModel().getOrderHelper().serialiseForApp(
				data,
				LocalDateTime.now(),
				DEFAULT_IS_CASH,
				DEFAULT_IS_HERE);
	}
	
	public void resetUserInput() {
		for (MenuItemEntry mie : this.menuItemEntries.toArray(MenuItemEntry[]::new)) {
			mie.removeFromParent();
		}
		this.refreshPrice();
	}
}
