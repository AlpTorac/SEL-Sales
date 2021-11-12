package client.view.composites;

import java.math.BigDecimal;
import java.util.Collection;

import controller.IController;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import model.order.IOrderItemData;
import view.repository.IChoiceBox;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ItemChangeListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class MenuItemEntry extends UIHBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private IChoiceBox<IDishMenuItemData> cb;
	private ITextBox amount;
	
	private PriceUpdateTarget<MenuItemEntry> notifyTarget;
	
	public MenuItemEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac, PriceUpdateTarget<MenuItemEntry> notifyTarget) {
		super(fac.createHBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.notifyTarget = notifyTarget;
		this.initComponent();
	}
	
	public MenuItemEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac, PriceUpdateTarget<MenuItemEntry> notifyTarget, IOrderItemData data) {
		this(controller, fac, advFac, notifyTarget);
		this.cb.artificiallySelectItem(data.getItemData());
		this.amount.setCaption(data.getAmount().toPlainString());
	}

	protected void initComponent() {
		this.cb = this.initChoiceBox();
		this.amount = this.initAmount();
		
		this.addUIComponents(new IUIComponent[] {
				this.cb,
				this.amount
		});
	}
	
	protected void choiceBoxInitExtra(IChoiceBox<IDishMenuItemData> choiceBox) {
		choiceBox.setEnabled(false);
		choiceBox.setOpacity(1);
	}
	
	protected IChoiceBox<IDishMenuItemData> initChoiceBox() {
		IChoiceBox<IDishMenuItemData> choiceBox = this.fac.createChoiceBox();
		choiceBox.addItemChangeListener(new ItemChangeListener() {
			public void selectedItemChanged(Object item) {
				notifyPriceDisplayingTarget();
			}
			
			public void itemRemovedAction(Collection<?> items) {
				notifyPriceDisplayingTarget();
			}
			
			public void itemAddedAction(Collection<?> items) {
				notifyPriceDisplayingTarget();
			}
			
			public void itemEditedAction(Collection<?> items) {
				notifyPriceDisplayingTarget();
			}
		});
		this.choiceBoxInitExtra(choiceBox);
		return choiceBox;
	}
	
	protected ITextBox initAmount() {
		ITextBox tb = this.fac.createTextBox();
		tb.setCaption("1");
		tb.setEnabled(false);
		tb.setOpacity(1);
		return tb;
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	protected IChoiceBox<IDishMenuItemData> getMenuItemChoiceBox() {
		return this.cb;
	}
	
	protected ITextBox getAmountTextBox() {
		return this.amount;
	}
	
	protected void removeFromParent() {
		this.notifyTarget.remove(this);
		this.dettach();
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.cb.clear();
		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
			this.addMenuItem(data);
		}
	}
	
	protected void addMenuItem(IDishMenuItemData data) {
		this.cb.addItem(data);
	}
	
	protected BigDecimal getAmount() {
		return BigDecimal.valueOf(Double.valueOf(this.amount.getText()));
	}
	
	public BigDecimal getPrice() {
		IDishMenuItemData data = null;
		if ((data = this.getSelectedMenuItem()) != null) {
			return data.getGrossPrice().multiply(this.getAmount());
		}
		return BigDecimal.ZERO;
	}
	
	protected void notifyPriceDisplayingTarget() {
		this.notifyTarget.refreshPrice();
	}
	
	public IDishMenuItemData getSelectedMenuItem() {
		return this.cb.getSelectedElement();
	}
}
