package client.view.composites;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import model.dish.IDishMenuData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderEntry extends UIVBoxLayout implements PriceUpdateTarget<MenuItemEntry> {

	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private PriceUpdateTarget<OrderEntry> notifyTarget;
	
	private IOrderData activeData;
	private IDishMenuData activeMenu;
	
	private Collection<MenuItemEntry> menuItemEntries;
	
	public OrderEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac, PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
		super(fac.createVBoxLayout().getComponent());
		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.notifyTarget = notifyTarget;
		this.activeData = data;
		this.initComponent();
	}

	protected void initComponent() {
		this.initMenuItemEntries(this.activeData);
	}
	
	protected void initMenuItemEntries(IOrderData data) {
		for (IOrderItemData itemData : data.getOrderedItems()) {
			this.addMenuItemEntry(itemData);
		}
	}
	
	protected void addMenuItemEntry(IOrderItemData data) {
		MenuItemEntry entry = this.createItemEntry(data);
		this.menuItemEntries.add(entry);
		this.addUIComponent(entry);
	}
	
	protected MenuItemEntry createItemEntry(IOrderItemData data) {
		return new MenuItemEntry(controller, fac, advFac, this, data);
	}
	
	@Override
	public void refreshPrice() {
		this.notifyTarget.refreshPrice();
	}

	protected void removeFromParent() {
		this.notifyTarget.remove(this);
		this.dettach();
	}
	
	@Override
	public void remove(MenuItemEntry referenceOfCaller) {
		this.menuItemEntries.remove(referenceOfCaller);
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.activeMenu = menuData;
		this.menuItemEntries.forEach(mie -> mie.refreshMenu(this.activeMenu));
	}
}
