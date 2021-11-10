package client.view.composites;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.IModel;
import model.dish.IDishMenuData;
import view.repository.IButton;
import view.repository.ILabel;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTakingArea extends UIVBoxLayout implements PriceUpdateTarget {
	private IModel model;
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	private IButton addMenuItemBtn;
	private ILabel priceDisplay;
	private IButton nextTabBtn;
	
	private String priceDisplayAffix = "Price: ";
	
	private IDishMenuData activeMenu;
	
	protected OrderTakingArea(IModel model, IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.model = model;
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
		return new EditableMenuItemEntry(this.model, this.controller, this.fac, this.advFac, this);
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
	public void remove(Object referenceOfCaller) {
		this.menuItemEntries.remove(referenceOfCaller);
	}
}
