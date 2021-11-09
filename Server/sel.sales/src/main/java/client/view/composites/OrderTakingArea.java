package client.view.composites;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.dish.IDishMenuData;
import view.repository.IButton;
import view.repository.IUILibraryHelper;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTakingArea extends UIVBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	private IButton addMenuItemBtn;
	
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
		this.setPrefHeight(800);
	}
	
	protected void addMenuItemEntry() {
		MenuItemEntry entry = this.createMenuItemEntry();
		this.menuItemEntries.add(entry);
		this.addUIComponentBefore(entry, this.addMenuItemBtn);
	}
	
	protected MenuItemEntry createMenuItemEntry() {
		return new EditableMenuItemEntry(this.controller, this.fac, this.advFac);
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.menuItemEntries.forEach(mie -> mie.refreshMenu(menuData));
	}
}
