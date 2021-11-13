package client.view.composites;

import client.view.composites.listener.AddCookingOrderListener;
import controller.IController;
import model.dish.IDishMenuData;
import model.order.IOrderData;
import view.repository.IButton;
import view.repository.IIndexedLayout;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTakingArea extends UIVBoxLayout implements PriceUpdateTarget<OrderEntry> {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private OrderEntry orderEntry;
	
//	private Collection<MenuItemEntry> menuItemEntries;
	
	private IDishMenuData activeMenu;
	
	protected OrderTakingArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
//		this.menuItemEntries = new CopyOnWriteArrayList<MenuItemEntry>();
		
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.init();
	}

	protected void displayOrder(IOrderData data) {
		this.orderEntry.displayData(data);
	}
	
	protected OrderEntry initOrderEntry() {
		return new OrderTakingAreaOrderEntry(controller, fac, advFac, this);
	}
	
	protected void init() {
		this.addUIComponent(this.orderEntry = this.initOrderEntry());
		this.orderEntry.refreshMenu(this.activeMenu);
		
//		this.addMenuItemBtn = this.fac.createButton();
//		this.addMenuItemBtn.setCaption("+");
//		this.addMenuItemBtn.addClickListener(new ClickEventListener() {
//			@Override
//			public void clickAction() {
//				orderEntry.createItemEntry();
//			}
//		});
//		this.addUIComponent(this.addMenuItemBtn);
		
//		UIHBoxLayout bottomPart = this.fac.createHBoxLayout();
//		bottomPart.setSpacing(50);
//		bottomPart.addUIComponents(new IUIComponent[] {
//			this.priceDisplay = this.initPriceDisplay(),
//					this.nextTabBtn = this.initNextTabButton()
//		});
//		
//		this.addUIComponent(bottomPart);
		
		this.setPrefHeight(800);
	}
	
//	protected IButton initNextTabButton() {
//		IButton btn = this.fac.createButton();
//		btn.setCaption("Add to cooking");
//		
//		ClickEventListener cel = new AddCookingOrderListener(this.orderEntry, this.controller);
//		btn.addClickListener(cel);
//		
//		return btn;
//	}

//	protected ILabel initPriceDisplay() {
//		ILabel label = this.fac.createLabel();
//		label.setCaption(this.priceDisplayAffix);
//		return label;
//	}

//	protected void refreshPriceDisplay() {
//		this.priceDisplay.setCaption(this.priceDisplayAffix + this.getNetPriceForDisplay());
//	}
	
//	protected String getNetPriceForDisplay() {
//		return this.getNetPrice().toPlainString();
//	}
	
//	protected BigDecimal getNetPrice() {
//		return this.menuItemEntries.stream().map(mie -> mie.getPrice()).reduce(BigDecimal.ZERO, (p1,p2)-> p1.add(p2));
//	}

//	protected void addMenuItemEntry() {
//		MenuItemEntry entry = this.createMenuItemEntry();
//		entry.refreshMenu(this.activeMenu);
//		this.menuItemEntries.add(entry);
//		this.addUIComponentBefore(entry, this.addMenuItemBtn);
//	}
	
//	protected MenuItemEntry createMenuItemEntry() {
//		return new EditableMenuItemEntry(this.controller, this.fac, this.advFac, this);
//	}
	
//	public void refreshMenu(IDishMenuData menuData) {
//		this.activeMenu = menuData;
//		this.menuItemEntries.forEach(mie -> mie.refreshMenu(this.activeMenu));
//	}
//
//	@Override
//	public void refreshPrice() {
//		this.refreshPriceDisplay();
//	}
//
//	@Override
//	public void remove(MenuItemEntry referenceOfCaller) {
//		this.menuItemEntries.remove(referenceOfCaller);
//	}
	
	public void refreshMenu(IDishMenuData menuData) {
		if (menuData != null) {
			this.activeMenu = menuData;
			this.orderEntry.refreshMenu(menuData);
		}
	}
	
	@Override
	public void refreshPrice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(OrderEntry referenceOfCaller) {
		
	}
	
	public class OrderTakingAreaOrderEntry extends OrderEntry {
		private IButton addMenuItemBtn;
		private IButton nextTabBtn;
		
		public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
				AdvancedUIComponentFactory advFac, PriceUpdateTarget<OrderEntry> notifyTarget) {
			super(controller, fac, advFac, notifyTarget);
		}
		public OrderTakingAreaOrderEntry(IController controller, UIComponentFactory fac,
				AdvancedUIComponentFactory advFac, PriceUpdateTarget<OrderEntry> notifyTarget, IOrderData data) {
			super(controller, fac, advFac, notifyTarget, data);
		}
		
		public MenuItemEntry createItemEntry() {
			return new EditableMenuItemEntry(controller, fac, advFac, this);
		}
		
		protected void initComponent() {
			this.addUIComponent(this.addMenuItemBtn = this.initAddMenuItemButton());
			super.initComponent();
		}
		
		protected IIndexedLayout initBottomPart() {
			IIndexedLayout bottom = super.initBottomPart();
			bottom.addUIComponent(this.nextTabBtn = this.initNextTabButton());
			return bottom;
		}
		
		protected IButton initAddMenuItemButton() {
			IButton btn = fac.createButton();
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
			IButton btn = fac.createButton();
			btn.setCaption("To cooking area");
			
			ClickEventListener cel = new AddCookingOrderListener(this, controller);
			btn.addClickListener(cel);
			
			return btn;
		}
		
		protected void addMenuItemEntryToUI(MenuItemEntry entry) {
			this.addUIComponentBefore(entry, this.addMenuItemBtn);
		}
	}
}
