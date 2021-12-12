package client.view.composites;

import java.math.BigDecimal;
import java.util.Collection;

import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import view.repository.IChoiceBox;
import view.repository.ISingleRowTextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.ItemChangeListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class MenuItemEntry extends UIHBoxLayout implements Cloneable {
	private UIComponentFactory fac;
	
	private IChoiceBox<DishMenuItemData> cb;
	private ISingleRowTextBox amount;
	
	private PriceUpdateTarget<MenuItemEntry> notifyTarget;
	
	private DishMenuData activeMenu;
	
	public MenuItemEntry(UIComponentFactory fac, PriceUpdateTarget<MenuItemEntry> notifyTarget) {
		super(fac.createHBoxLayout().getComponent());
		this.fac = fac;
		this.notifyTarget = notifyTarget;
		this.initComponent();
	}

	protected void initComponent() {
		this.cb = this.initChoiceBox();
		this.amount = this.initAmount();
		
		this.addUIComponents(new IUIComponent[] {
				this.cb,
				this.amount
		});
	}
	
	public void displayData(AccumulatingAggregateEntry<DishMenuItemData> data) {
		if (data != null) {
			this.selectMenuItem(data.getItem());
			this.setAmount(data.getAmount().intValue());
		}
//		this.cb.artificiallySelectItem(data.getItemData());
//		this.amount.setCaption(data.getAmount().toPlainString());
	}
	
	protected void choiceBoxInitExtra(IChoiceBox<DishMenuItemData> choiceBox) {
		choiceBox.setEnabled(false);
		choiceBox.setOpacity(1);
	}
	
	protected IChoiceBox<DishMenuItemData> initChoiceBox() {
		IChoiceBox<DishMenuItemData> choiceBox = this.fac.createChoiceBox();
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
			// Commented out, since it is never used. Enable, if necessary
//			public void itemEditedAction(Collection<?> items) {
//				notifyPriceDisplayingTarget();
//			}
		});
		this.choiceBoxInitExtra(choiceBox);
		choiceBox.setPrefWidth(100);
		return choiceBox;
	}
	
	protected ISingleRowTextBox initAmount() {
		ISingleRowTextBox tb = this.fac.createSingleRowTextBox();
		tb.setCaption("1");
		tb.setEnabled(false);
		tb.setOpacity(1);
		tb.setPrefWidth(40);
		return tb;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	public IChoiceBox<DishMenuItemData> getMenuItemChoiceBox() {
		return this.cb;
	}
	
	public ISingleRowTextBox getAmountTextBox() {
		return this.amount;
	}
	
	protected void removeFromParent() {
		this.getNotifyTarget().remove(this);
		this.dettach();
	}
	
	protected void setActiveMenu(DishMenuData menuData) {
		this.activeMenu = menuData;
	}
	
	public DishMenuData getActiveMenu() {
		return this.activeMenu;
	}
	
	public void refreshMenu(DishMenuData menuData) {
		DishMenuItemData selection = this.getSelectedMenuItem();
		if (menuData != null) {
			this.setActiveMenu(menuData);
			this.cb.clear();
			for (DishMenuItemData data : this.getActiveMenu().getAllElements()) {
				this.addMenuItem(data);
			}
		}
		this.selectMenuItem(selection);
		this.getNotifyTarget().refreshPrice();
	}
	
	protected void selectMenuItem(DishMenuItemData selection) {
		if (selection != null) {
			this.cb.artificiallySelectItem(selection);
		}
	}
	
	protected void setAmount(int amount) {
		this.amount.setCaption(String.valueOf(amount));
	}
	
	protected void addMenuItem(DishMenuItemData data) {
		this.cb.addItem(data);
	}
	
	public BigDecimal getAmount() {
		return BigDecimal.valueOf(Double.valueOf(this.amount.getText()));
	}
	
	public BigDecimal getPrice() {
		DishMenuItemData data = null;
		if ((data = this.getSelectedMenuItem()) != null) {
			return data.getGrossPrice().multiply(this.getAmount());
		}
		return BigDecimal.ZERO;
	}
	
	protected void notifyPriceDisplayingTarget() {
		this.getNotifyTarget().refreshPrice();
	}
	
	public DishMenuItemData getSelectedMenuItem() {
		return this.cb.getSelectedElement();
	}
	
	protected PriceUpdateTarget<MenuItemEntry> getNotifyTarget() {
		return this.notifyTarget;
	}
	
	protected MenuItemEntry constructClone() {
		return new MenuItemEntry(this.getUIFactory(), this.getNotifyTarget());
	}
	
	public MenuItemEntry clone() {
		MenuItemEntry clone = this.constructClone();
		clone.refreshMenu(this.getActiveMenu());
		clone.selectMenuItem(this.getSelectedMenuItem());
		clone.setAmount(this.getAmount().intValue());
		return clone;
	}
}
